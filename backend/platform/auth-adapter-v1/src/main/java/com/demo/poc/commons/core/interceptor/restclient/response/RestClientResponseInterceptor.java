package com.demo.poc.commons.core.interceptor.restclient.response;

import com.demo.poc.commons.core.logging.ThreadContextInjector;
import com.demo.poc.commons.core.logging.dto.RestResponseLog;
import com.demo.poc.commons.core.logging.enums.LoggingType;
import com.demo.poc.commons.core.tracing.enums.TraceParam;
import com.demo.poc.commons.custom.properties.ApplicationProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;

import java.util.HashMap;

@Slf4j
@RequiredArgsConstructor
public class RestClientResponseInterceptor implements ExchangeFilterFunction {

  private final ThreadContextInjector contextInjector;
  private final ApplicationProperties properties;

  @Override
  public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction next) {
    return next.exchange(request)
        .flatMap(clientResponse -> clientResponse.bodyToMono(String.class)
            .defaultIfEmpty(StringUtils.EMPTY)
            .map(responseBody -> {

              generateTrace(request, clientResponse, responseBody);

              return ClientResponse.create(clientResponse.statusCode())
                  .headers(headers -> headers.addAll(clientResponse.headers().asHttpHeaders()))
                  .body(responseBody)
                  .build();
            }));
  }

  private void generateTrace(ClientRequest request, ClientResponse response, String responseBody) {
    boolean isLoggerPresent = properties.isLoggerPresent(LoggingType.REST_CLIENT_RES);
    if (isLoggerPresent) {
      RestResponseLog log = RestResponseLog.builder()
          .uri(request.url().toString())
          .responseBody(responseBody)
          .responseHeaders(new HashMap<>(response.headers().asHttpHeaders().toSingleValueMap()))
          .httpCode(String.valueOf(response.statusCode().value()))
          .traceParent(request.headers().getFirst(TraceParam.TRACE_PARENT.getKey()))
          .build();

      contextInjector.populateFromRestResponse(LoggingType.REST_CLIENT_RES, log);
    }
  }

}