package com.demo.bbq.application.filter;

import com.demo.bbq.application.properties.ServiceConfigurationProperties;
import com.demo.bbq.repository.authadapter.AuthAdapterRepository;
import com.demo.bbq.utils.errors.exceptions.AuthorizationException;
import com.demo.bbq.utils.errors.handler.ResponseErrorUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;

@Slf4j
@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

  private final AuthAdapterRepository authAdapterRepository;
  private final ServiceConfigurationProperties configurationProperties;

  public AuthenticationFilter(AuthAdapterRepository authAdapterRepository,
                              ServiceConfigurationProperties configurationProperties) {
    super(Config.class);
    this.authAdapterRepository = authAdapterRepository;
    this.configurationProperties = configurationProperties;
  }

  @Override
  public GatewayFilter apply(Config config) {
    return new OrderedGatewayFilter((exchange, chain) -> Flux.fromIterable(getExpectedRoles())
        .flatMap(expectedRol -> authAdapterRepository.getRoles(getAuthToken(exchange))
            .map(roles -> {
              if(!roles.containsKey(expectedRol)) {
                throw new AuthorizationException("ExpectedRoleNotFound", "The expected role was not found");
              }
              return roles;
            }))
        .ignoreElements()
        .then(chain.filter(exchange))
        .onErrorResume(Exception.class, exception -> ResponseErrorUtil.handleException(configurationProperties, exception, exchange)),1);
  }

  private String getAuthToken(ServerWebExchange serverWebExchange) {
    HttpHeaders httpHeaders = serverWebExchange.getRequest().getHeaders();

    if (!httpHeaders.containsKey(HttpHeaders.AUTHORIZATION)) {
      throw new AuthorizationException("MissingAuthorizationHeader", "Missing Authorization header");
    }

    String authorizationHeader = httpHeaders.get(HttpHeaders.AUTHORIZATION).get(0);
    String[] authElements = authorizationHeader.split(" ");
    if (authElements.length != 2 || !"Bearer".equals(authElements[0])) {
      throw new AuthorizationException("InvalidAuthorizationStructure", "Invalid authorization structure");
    }

    return authElements[1];
  }

  private List<String> getExpectedRoles() {
    String[] roles = authAdapterRepository.getVariables().get("expected-roles").split(",");
    return new ArrayList<>(Arrays.asList(roles));
  }

  public static class Config {}
}
