package com.demo.poc.commons.core.interceptor.restserver;

import java.util.HashMap;

import com.demo.poc.commons.core.logging.ThreadContextInjector;
import com.demo.poc.commons.core.logging.dto.RestRequestLog;
import com.demo.poc.commons.core.logging.dto.RestResponseLog;
import com.demo.poc.commons.core.logging.enums.LoggingType;
import com.demo.poc.commons.core.tracing.enums.TraceParam;
import com.demo.poc.commons.custom.properties.ApplicationProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
public class RestServerInterceptor implements WebFilter {

  private final ThreadContextInjector contextInjector;
  private final ApplicationProperties properties;

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
    String uri = exchange.getRequest().getURI().toString();
    String traceParent = exchange.getRequest().getHeaders().getFirst(TraceParam.TRACE_PARENT.getKey());

    generateTraceOfRequest(exchange.getRequest());

    return chain.filter(exchange)
        .doOnSuccess(ignore -> generateTraceOfResponse(exchange.getResponse(), uri, traceParent));
  }

  private void generateTraceOfRequest(ServerHttpRequest serverHttpRequest) {
    boolean isLoggerRequestPresent = properties.isLoggerPresent(LoggingType.REST_SERVER_REQ);
    if(isLoggerRequestPresent) {
      RestRequestLog log = RestRequestLog.builder()
          .uri(serverHttpRequest.getURI().toString())
          .method(serverHttpRequest.getMethod().toString())
          .requestHeaders(serverHttpRequest.getHeaders().toSingleValueMap())
          .requestBody("ToDo")
          .traceParent(serverHttpRequest.getHeaders().getFirst(TraceParam.TRACE_PARENT.getKey()))
          .build();
      contextInjector.populateFromRestRequest(LoggingType.REST_SERVER_REQ, log);
    }
  }

  private void generateTraceOfResponse(ServerHttpResponse serverHttpResponse, String uri, String traceParent) {
    boolean isLoggerResponsePresent = properties.isLoggerPresent(LoggingType.REST_SERVER_RES);
    if(isLoggerResponsePresent) {
      RestResponseLog log = RestResponseLog.builder()
          .uri(uri)
          .responseHeaders(new HashMap<>(serverHttpResponse.getHeaders().toSingleValueMap()))
          .httpCode(serverHttpResponse.getStatusCode().toString())
          .responseBody("ToDo")
          .traceParent(traceParent)
          .build();

      contextInjector.populateFromRestResponse(LoggingType.REST_SERVER_RES, log);
    }
  }
}
