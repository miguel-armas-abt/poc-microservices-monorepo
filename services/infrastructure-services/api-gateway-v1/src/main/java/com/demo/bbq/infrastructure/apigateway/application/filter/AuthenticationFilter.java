package com.demo.bbq.infrastructure.apigateway.application.filter;

import com.demo.bbq.infrastructure.apigateway.infrastructure.repository.restclient.authadapter.AuthAdapterApi;
import com.demo.bbq.infrastructure.apigateway.infrastructure.properties.RolesProperties;
import com.demo.bbq.infrastructure.apigateway.domain.exception.ApiGatewayException;
import io.reactivex.BackpressureStrategy;
import java.util.ArrayList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.adapter.rxjava.RxJava2Adapter;
import reactor.core.publisher.Flux;

@Slf4j
@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

  private final AuthAdapterApi authAdapterApi;
  private final RolesProperties properties;

  public AuthenticationFilter(AuthAdapterApi authAdapterApi, RolesProperties properties) {
    super(Config.class);
    this.authAdapterApi = authAdapterApi;
    this.properties = properties;
  }

  @Override
  public GatewayFilter apply(Config config) {
    return new OrderedGatewayFilter((serverWebExchange, chain) -> {
      ArrayList<String> rolesList = new ArrayList<>(properties.getRolesList().values());

      return Flux.fromIterable(rolesList)
          .flatMap(expectedRol -> RxJava2Adapter.observableToFlux(authAdapterApi.getRoles(getAuthToken(serverWebExchange)), BackpressureStrategy.BUFFER)
              .map(hashMap -> {
                if(!hashMap.containsKey(expectedRol)) {
                  throw ApiGatewayException.ERROR0002.buildException();
                }
                return hashMap;
              }))
          .ignoreElements()
          .then(chain.filter(serverWebExchange).onErrorMap(ApiGatewayException.ERROR0003::buildException));
    },1);
  }

  private String getAuthToken(ServerWebExchange serverWebExchange) {
    HttpHeaders httpHeaders = serverWebExchange.getRequest().getHeaders();

    if (!httpHeaders.containsKey(HttpHeaders.AUTHORIZATION)) {
      throw ApiGatewayException.ERROR0000.buildException();
    }

    String authorizationHeader = httpHeaders.get(HttpHeaders.AUTHORIZATION).get(0);
    String[] authElements = authorizationHeader.split(" ");

    if (authElements.length != 2 || !"Bearer".equals(authElements[0])) {
      throw ApiGatewayException.ERROR0001.buildException();
    }

    return authElements[1];
  }

  public static class Config {}
}
