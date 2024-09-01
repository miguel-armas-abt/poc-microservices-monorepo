package com.demo.bbq.commons.tracing.logging.restserver;

import com.demo.bbq.commons.properties.ConfigurationBaseProperties;
import com.demo.bbq.commons.tracing.logging.constants.LoggingExclusion;
import com.demo.bbq.commons.tracing.logging.enums.LoggerType;
import com.demo.bbq.commons.tracing.logging.injector.ThreadContextInjector;
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
public class RestServerLogger implements WebFilter {

  private final ThreadContextInjector threadContextInjector;
  private final ConfigurationBaseProperties properties;

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
    String url = exchange.getRequest().getURI().toString();

    if (LoggingExclusion.isExcluded(url)) {
      return chain.filter(exchange);
    }

    generateTraceOfRequest(exchange.getRequest(), url);
    return chain.filter(exchange).doOnSuccess(aVoid -> generateTraceOfResponse(exchange.getResponse()));
    }

    private void generateTraceOfRequest(ServerHttpRequest serverHttpRequest, String url) {
      if (properties.isLoggerPresent(LoggerType.REST_SERVER_REQ)) {
        threadContextInjector.populateFromRestServerRequest(
            serverHttpRequest.getMethod().toString(),
            url,
            serverHttpRequest.getHeaders().toSingleValueMap(),
            "ToDo");
      }
    }

    private void generateTraceOfResponse(ServerHttpResponse serverHttpResponse) {
      if (properties.isLoggerPresent(LoggerType.REST_SERVER_RES)) {
        threadContextInjector.populateFromRestServerResponse(
            serverHttpResponse.getHeaders().toSingleValueMap(),
            "ToDo",
            serverHttpResponse.getStatusCode().toString());
      }
    }
}
