package com.demo.bbq.infrastructure.apigateway.setups;

import com.demo.bbq.infrastructure.apigateway.repository.proxy.AuthAdapterProxy;
import com.demo.bbq.infrastructure.apigateway.util.config.ApplicationProperties;
import com.demo.bbq.infrastructure.apigateway.util.exception.ApiGatewayException;
import io.reactivex.BackpressureStrategy;
import java.util.ArrayList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import reactor.adapter.rxjava.RxJava2Adapter;
import reactor.core.publisher.Flux;

@Slf4j
@Component
public class AuthenticatorFiltering extends AbstractGatewayFilterFactory<AuthenticatorFiltering.Config> {

  private final AuthAdapterProxy authAdapterProxy;
  private final ApplicationProperties properties;

  public AuthenticatorFiltering(AuthAdapterProxy authAdapterProxy, ApplicationProperties properties) {
    super(Config.class);
    this.authAdapterProxy = authAdapterProxy;
    this.properties = properties;
  }

  @Override
  public GatewayFilter apply(Config config) {
    return new OrderedGatewayFilter((exchange, chain) -> {
      ArrayList<String> rolesList = new ArrayList<>(properties.getRolesList().values());

      if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
        throw ApiGatewayException.ERROR0000.buildException();
      }

      String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
      String[] parts = authHeader.split(" ");
      if (parts.length != 2 || !"Bearer".equals(parts[0])) {
        throw ApiGatewayException.ERROR0001.buildException();
      }

      return Flux.fromIterable(rolesList)
          .flatMap(expectedRol -> RxJava2Adapter.observableToFlux(authAdapterProxy.listRoles(parts[1]), BackpressureStrategy.BUFFER)
              .map(hashMap -> {
                if(!hashMap.containsKey(expectedRol)) {
                  throw ApiGatewayException.ERROR0002.buildException();
                }
                return hashMap;
              }))
          .ignoreElements()
          .then(chain.filter(exchange).onErrorMap(ApiGatewayException.ERROR0003::buildException));
    },1);
  }

  public static class Config {}
}
