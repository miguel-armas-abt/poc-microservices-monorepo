package com.demo.bbq.commons.tracing.logging.restclient.response;

import com.demo.bbq.commons.properties.ConfigurationBaseProperties;
import com.demo.bbq.commons.tracing.logging.enums.LoggerType;
import com.demo.bbq.commons.tracing.logging.injector.ThreadContextInjector;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
public class RestClientResponseLogger implements ExchangeFilterFunction {

  private final ThreadContextInjector threadContextInjector;
  private final ConfigurationBaseProperties properties;

  @Override
  public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction next) {
    return next.exchange(request)
        .flatMap(this::decorateResponse);
  }

  private Mono<ClientResponse> decorateResponse(ClientResponse clientResponse) {
    return clientResponse
        .bodyToMono(String.class)
        .defaultIfEmpty(StringUtils.EMPTY)
        .flatMap(responseBody -> {
          generateTraceIfLoggerIsPresent(clientResponse, responseBody);
          return Mono.just(ClientResponse.create(clientResponse.statusCode())
              .headers(headers -> headers.addAll(clientResponse.headers().asHttpHeaders()))
              .body(responseBody)
              .build());
        });
  }

  private void generateTraceIfLoggerIsPresent(ClientResponse response, String responseBody) {
    if(properties.isLoggerPresent(LoggerType.REST_CLIENT_RES))
      generateTrace(response, responseBody);
  }

  private void generateTrace(ClientResponse response, String responseBody) {
    try {
      threadContextInjector.populateFromRestClientResponse(
          response.headers().asHttpHeaders().toSingleValueMap(),
          responseBody,
          getHttpCode(response));
    } catch (Exception ex) {
      log.error("Error reading response body: {}", ex.getClass(), ex);
    }
  }

  private static String getHttpCode(ClientResponse response) {
    try {
      return response.statusCode().toString();
    } catch (IllegalArgumentException ex) {
      return String.valueOf(response.statusCode());
    }
  }
}