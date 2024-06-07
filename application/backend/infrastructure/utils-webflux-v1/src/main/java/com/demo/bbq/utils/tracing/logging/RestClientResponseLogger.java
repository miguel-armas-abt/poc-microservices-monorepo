package com.demo.bbq.utils.tracing.logging;

import static com.demo.bbq.utils.tracing.logging.constants.LoggingMessage.REST_CLIENT_RESPONSE_ERROR;
import static com.demo.bbq.utils.tracing.logging.constants.LoggingMessage.REST_CLIENT_RESPONSE;
import static com.demo.bbq.utils.tracing.logging.constants.ThreadContextConstant.TRACKING_INFO;

import com.demo.bbq.utils.properties.ConfigurationBaseProperties;
import com.demo.bbq.utils.tracing.logging.injector.ThreadContextInjectorUtil;
import com.demo.bbq.utils.tracing.logging.obfuscation.body.BodyObfuscatorUtil;
import com.demo.bbq.utils.tracing.logging.obfuscation.header.HeaderObfuscatorUtil;
import com.demo.bbq.utils.tracing.logging.util.HeaderMapperUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

@Slf4j
@RequiredArgsConstructor
public class RestClientResponseLogger implements ExchangeFilterFunction {

  private final ConfigurationBaseProperties properties;

  @Override
  public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction next) {
    var context = captureRequest(request);
    return next.exchange(request)
        .contextWrite(context)
        .flatMap(response -> decorateResponse(properties, request, response, context));
  }

  private static Context captureRequest(ClientRequest request) {
    var httpHeaders = request.headers();
    return Context.of(TRACKING_INFO, httpHeaders);
  }

  private static Mono<ClientResponse> decorateResponse(ConfigurationBaseProperties properties,
                                                       ClientRequest clientRequest,
                                                       ClientResponse clientResponse,
                                                       Context context) {
    return clientResponse
        .bodyToMono(String.class)
        .defaultIfEmpty(StringUtils.EMPTY)
        .flatMap(responseBody -> {

          generateLog(properties, clientRequest, clientResponse, responseBody, context);
          var responseHeaders = clientResponse.headers().asHttpHeaders();

          return Mono.just(ClientResponse.create(clientResponse.statusCode())
              .headers(headers -> headers.addAll(responseHeaders))
              .body(responseBody)
              .build());
        });
  }

  private static void generateLog(ConfigurationBaseProperties properties,
                                  ClientRequest clientRequest,
                                  ClientResponse response,
                                  String responseBody,
                                  Context context) {
    try {
      context.getOrEmpty(TRACKING_INFO)
          .ifPresent(httpHeaders -> ThreadContextInjectorUtil.populateFromHeaders(HeaderMapperUtil.recoverTraceHeaders((HttpHeaders) httpHeaders)));

      var method = clientRequest.method().toString();
      var uri = clientRequest.url().toString();
      var headers = HeaderObfuscatorUtil.process(properties.getObfuscation(), response.headers().asHttpHeaders().toSingleValueMap());
      var body = BodyObfuscatorUtil.process(properties.getObfuscation(), responseBody);

      ThreadContextInjectorUtil.populateFromRestClientResponse(method, uri, headers, body, getHttpCode(response));
      log.info(REST_CLIENT_RESPONSE);

    } catch (Exception ex) {
      log.error(REST_CLIENT_RESPONSE_ERROR + ex.getClass(), ex);
    }
    ThreadContext.clearAll();
  }

  private static String getHttpCode(ClientResponse response) {
    try {
      return response.statusCode().toString();
    } catch (IllegalArgumentException ex) {
      return String.valueOf(response.statusCode());
    }
  }
}