package com.demo.bbq.commons.tracing.logging.restclient.request;

import static org.apache.commons.lang3.StringUtils.EMPTY;

import com.demo.bbq.commons.properties.ConfigurationBaseProperties;
import com.demo.bbq.commons.tracing.logging.enums.LoggerType;
import com.demo.bbq.commons.tracing.logging.injector.ThreadContextInjector;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

  private final ThreadContextInjector threadContextInjector;
  private final ConfigurationBaseProperties properties;

  @Override
  public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction next) {
    return next.exchange(decorateRequest(request));
  }

  private ClientRequest decorateRequest(ClientRequest clientRequest) {
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
                    generateTraceIfLoggerIsPresent(clientRequest, requestBody);

                    // buffer release
                    DataBuffer replicaBuffer = buffer.factory().wrap(buffer.asByteBuffer());
                    return super.writeWith(Mono.just(replicaBuffer))
                        .doFinally(signalType -> DataBufferUtils.release(buffer));
                  });
            }

            @Override
            public Mono<Void> setComplete() {
              generateTraceIfLoggerIsPresent(clientRequest, EMPTY);
              return super.setComplete();
            }

          };
          return requestBody.insert(decorator, context);
        })
        .build();
  }

  private void generateTraceIfLoggerIsPresent(ClientRequest clientRequest, String requestBody) {
    if (properties.isLoggerPresent(LoggerType.REST_CLIENT_REQ))
      generateTrace(clientRequest, requestBody);
  }

  private void generateTrace(ClientRequest clientRequest, String requestBody) {
    try {
      threadContextInjector.populateFromRestClientRequest(
          clientRequest.method().toString(),
          clientRequest.url().toString(),
          clientRequest.headers().toSingleValueMap(),
          requestBody);
    } catch (Exception ex) {
      log.error("Error reading request body: {}", ex.getClass(), ex);
    }
  }
}