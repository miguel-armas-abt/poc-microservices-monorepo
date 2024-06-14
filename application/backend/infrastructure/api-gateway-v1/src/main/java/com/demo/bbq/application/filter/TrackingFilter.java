package com.demo.bbq.application.filter;

import static com.demo.bbq.commons.properties.dto.GeneratedHeaderType.TRACE_ID;

import com.demo.bbq.config.properties.ServiceConfigurationProperties;
import com.demo.bbq.commons.errors.handler.response.ResponseErrorHandlerUtil;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

@Slf4j
@Component
public class TrackingFilter extends AbstractGatewayFilterFactory<TrackingFilter.Config> {

  private final ServiceConfigurationProperties configurationProperties;

  public TrackingFilter(ServiceConfigurationProperties configurationProperties) {
    super(Config.class);
    this.configurationProperties = configurationProperties;
  }

  @Override
  public GatewayFilter apply(Config config) {
    return new OrderedGatewayFilter((exchange, chain) ->
        chain.filter(updateHeaders(exchange))
          .onErrorResume(Exception.class, exception -> ResponseErrorHandlerUtil.handleException(configurationProperties, exception, exchange))
        , 0);
  }

  private static ServerWebExchange updateHeaders(ServerWebExchange exchange) {
    return Optional.ofNullable(exchange.getRequest().getHeaders().getFirst(TRACE_ID.getKey())).isPresent()
        ? exchange
        : mutateExchangeRequest(exchange);
  }

  private static ServerWebExchange mutateExchangeRequest(ServerWebExchange exchange) {
    String traceId = TRACE_ID.getHeaderGenerator().generateHeader();

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