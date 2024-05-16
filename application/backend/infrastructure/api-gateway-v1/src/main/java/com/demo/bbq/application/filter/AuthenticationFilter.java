package com.demo.bbq.application.filter;

import com.demo.bbq.config.properties.ServiceConfigurationProperties;
import com.demo.bbq.application.utils.TokenValidatorUtil;
import com.demo.bbq.repository.authadapter.AuthAdapterRepository;
import com.demo.bbq.utils.errors.exceptions.AuthorizationException;
import com.demo.bbq.utils.errors.handler.response.ResponseErrorHandlerUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
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
    return new OrderedGatewayFilter((exchange, chain) -> {
      TokenValidatorUtil.getAuthToken(exchange);
      return Flux.fromIterable(getExpectedRoles())
          .flatMap(expectedRol -> authAdapterRepository.getRoles(exchange.getRequest())
              .map(roles -> {
                if(!roles.containsKey(expectedRol))
                  throw new AuthorizationException("ExpectedRoleNotFound", "The expected role was not found");
                return roles;
              }))
          .ignoreElements()
          .then(chain.filter(exchange))
          .onErrorResume(Exception.class, exception -> ResponseErrorHandlerUtil.handleException(configurationProperties, exception, exchange));
    } ,1);
  }

  private List<String> getExpectedRoles() {
    String[] roles = authAdapterRepository.getVariables().get("expected-roles").split(",");
    return new ArrayList<>(Arrays.asList(roles));
  }

  public static class Config {}
}
