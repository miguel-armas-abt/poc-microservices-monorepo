package com.demo.bbq.utils.tracing.logging;

import static com.demo.bbq.utils.tracing.logging.constants.LoggingMessage.REST_CLIENT_REQUEST_ERROR;
import static com.demo.bbq.utils.tracing.logging.constants.LoggingMessage.REST_CLIENT_REQUEST;
import static org.apache.commons.lang3.StringUtils.EMPTY;

import com.demo.bbq.utils.properties.ConfigurationBaseProperties;
import com.demo.bbq.utils.tracing.logging.injector.ThreadContextInjectorUtil;
import com.demo.bbq.utils.tracing.logging.obfuscation.body.BodyObfuscatorUtil;
import com.demo.bbq.utils.tracing.logging.obfuscation.header.HeaderObfuscatorUtil;
import com.demo.bbq.utils.tracing.logging.util.HeaderMapperUtil;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.ThreadContext;
import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.client.reactive.ClientHttpRequest;
import org.springframework.http.client.reactive.ClientHttpRequestDecorator;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
public class RestClientRequestLogger implements ExchangeFilterFunction {

  private final ConfigurationBaseProperties properties;

  @Override
  public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction next) {
    return next.exchange(decorateRequest(properties, request));
  }

  private static ClientRequest decorateRequest(ConfigurationBaseProperties properties,
                                               ClientRequest clientRequest) {

    BodyInserter<?, ? super ClientHttpRequest> requestBody = clientRequest.body();

    return ClientRequest
        .from(clientRequest)
        .body((clientHttpRequest, context) -> {
          ClientHttpRequestDecorator decorator = new ClientHttpRequestDecorator(clientHttpRequest) {

            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
              return DataBufferUtils
                  .join(body)
                  .flatMap(buffer -> {

                    var requestBody = buffer.toString(StandardCharsets.UTF_8);
                    generateLog(properties, clientRequest, requestBody);

                    // buffer release
                    DataBuffer replicaBuffer = buffer.factory().wrap(buffer.asByteBuffer());
                    return super.writeWith(Mono.just(replicaBuffer))
                        .doFinally(signalType -> DataBufferUtils.release(buffer));
                  });
            }

            @Override
            public Mono<Void> setComplete() {
              generateLog(properties, clientRequest, EMPTY);
              return super.setComplete();
            }

          };
          return requestBody.insert(decorator, context);
        })
        .build();
  }

  private static void generateLog(ConfigurationBaseProperties properties,
                                  ClientRequest clientRequest,
                                  String requestBody) {
    try {
      var method = clientRequest.method().toString();
      var uri = clientRequest.url().toString();
      var httpHeaders = clientRequest.headers();
      var headers = HeaderObfuscatorUtil.process(properties.getObfuscation(), httpHeaders.toSingleValueMap());
      var body = BodyObfuscatorUtil.process(properties.getObfuscation(), requestBody);

      ThreadContextInjectorUtil.populateFromHeaders(HeaderMapperUtil.recoverTraceHeaders(httpHeaders));
      ThreadContextInjectorUtil.populateFromRestClientRequest(method, uri, headers, body);
      log.info(REST_CLIENT_REQUEST);

    } catch (Exception ex) {
      log.error(REST_CLIENT_REQUEST_ERROR + ex.getClass(), ex);
    }
    ThreadContext.clearAll();
  }
}