package com.demo.bbq.infrastructure.apigateway.setups;

import com.demo.bbq.infrastructure.apigateway.repository.proxy.AuthAdapterProxy;
import com.demo.bbq.infrastructure.apigateway.util.config.ApplicationProperties;
import com.demo.bbq.infrastructure.apigateway.util.exception.ExceptionCatalog;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

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
        throw ExceptionCatalog.ERROR1000.buildException();
      }

      String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
      String[] parts = authHeader.split(" ");
      if (parts.length != 2 || !"Bearer".equals(parts[0])) {
        throw ExceptionCatalog.ERROR1001.buildException();
      }

      List<String> actualRolesList = authAdapterProxy.listRoles(parts[1]).blockingFirst()
          .entrySet()
          .stream()
          .map(Map.Entry::getKey)
          .collect(Collectors.toList());

      rolesList.forEach(rol -> {
            if (!actualRolesList.contains(rol)) {
              throw ExceptionCatalog.ERROR1002.buildException();
            }
          });

      return chain.filter(exchange)
          .onErrorMap(ExceptionCatalog.ERROR1003::buildException);
    },1);
  }

  public static class Config {}
}
