package com.demo.poc.entrypoint.tracking.filter;

import com.demo.poc.commons.core.interceptor.error.ErrorInterceptor;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import static com.demo.poc.commons.core.tracing.enums.TraceParamType.TRACE_ID;

@Slf4j
@Component
public class TrackingFilter extends AbstractGatewayFilterFactory<TrackingFilter.Config> {

  private final ErrorInterceptor errorInterceptor;

  public TrackingFilter(ErrorInterceptor errorInterceptor) {
    super(Config.class);
    this.errorInterceptor = errorInterceptor;
  }

  @Override
  public GatewayFilter apply(Config config) {
    return new OrderedGatewayFilter((exchange, chain) ->
        chain.filter(updateHeaders(exchange))
          .onErrorResume(Exception.class, exception -> errorInterceptor.handleException(exception, exchange))
        , 0);
  }

  private static ServerWebExchange updateHeaders(ServerWebExchange exchange) {
    return Optional.ofNullable(exchange.getRequest().getHeaders().getFirst(TRACE_ID.getKey())).isPresent()
        ? exchange
        : mutateExchangeRequest(exchange);
  }

  private static ServerWebExchange mutateExchangeRequest(ServerWebExchange exchange) {
    String traceId = TRACE_ID.getParamGenerator().generateParam();

    ServerHttpRequest modifiedRequest = exchange.getRequest()
        .mutate()
        .header(TRACE_ID.getKey(), traceId)
        .build();

    return exchange.mutate()
        .request(modifiedRequest)
        .build();
  }

  public static class Config {}
}