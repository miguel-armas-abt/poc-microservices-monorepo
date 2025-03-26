package com.demo.bbq.commons.core.interceptor.restclient.response;

import com.demo.bbq.commons.core.logging.ThreadContextInjector;
import com.demo.bbq.commons.core.logging.enums.LoggingType;
import com.demo.bbq.commons.core.properties.ConfigurationBaseProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

import static com.demo.bbq.commons.core.tracing.enums.TraceParamType.TRACE_ID;

@Slf4j
@RequiredArgsConstructor
public class RestClientResponseInterceptor implements ExchangeFilterFunction {

  private static final String SAME_REQUEST = "rest.client";

  private final ThreadContextInjector threadContextInjector;
  private final ConfigurationBaseProperties properties;

  @Override
  public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction next) {
    String traceId = request.headers().getFirst(TRACE_ID.getKey());
    return next.exchange(request)
        .flatMap(clientResponse -> decorateResponse(clientResponse, traceId));
  }

  private Mono<ClientResponse> decorateResponse(ClientResponse clientResponse, String traceId) {
    return clientResponse
        .bodyToMono(String.class)
        .defaultIfEmpty(StringUtils.EMPTY)
        .flatMap(responseBody -> {
          generateTraceIfLoggerIsPresent(clientResponse, responseBody, traceId);
          return Mono.just(ClientResponse.create(clientResponse.statusCode())
              .headers(headers -> headers.addAll(clientResponse.headers().asHttpHeaders()))
              .body(responseBody)
              .build());
        });
  }

  private void generateTraceIfLoggerIsPresent(ClientResponse response, String responseBody, String traceId) {
    if(properties.isLoggerPresent(LoggingType.REST_CLIENT_RES))
      generateTrace(response, responseBody, traceId);
  }

  private void generateTrace(ClientResponse response, String responseBody, String traceId) {
    try {
      Map<String, String> headers = new HashMap<>(response.headers().asHttpHeaders().toSingleValueMap());
      headers.put(TRACE_ID.getKey(), traceId);

      threadContextInjector.populateFromRestClientResponse(
          headers,
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