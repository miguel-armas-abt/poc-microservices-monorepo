package com.demo.service.entrypoint.tracking.filter;

import com.demo.commons.interceptor.error.ErrorInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

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
        chain.filter(exchange)
          .onErrorResume(Exception.class, exception -> errorInterceptor.handleException(exception, exchange))
        , 0);
  }

  public static class Config {}
}