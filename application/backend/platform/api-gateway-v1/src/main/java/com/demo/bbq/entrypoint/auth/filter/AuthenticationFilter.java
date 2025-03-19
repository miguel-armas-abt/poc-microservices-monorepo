package com.demo.bbq.entrypoint.auth.filter;

import com.demo.bbq.commons.interceptor.error.ErrorInterceptor;
import com.demo.bbq.commons.properties.ApplicationProperties;
import com.demo.bbq.entrypoint.auth.utils.TokenValidatorUtil;
import com.demo.bbq.entrypoint.auth.repository.AuthAdapterRepository;
import com.demo.bbq.commons.errors.exceptions.AuthorizationException;
import java.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static com.demo.bbq.commons.restclient.utils.HttpHeadersFiller.extractHeadersAsMap;

@Slf4j
@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

  private final AuthAdapterRepository authAdapterRepository;
  private final ApplicationProperties properties;
  private final ErrorInterceptor responseErrorHandler;

  public AuthenticationFilter(AuthAdapterRepository authAdapterRepository,
                              ApplicationProperties properties,
                              ErrorInterceptor errorInterceptor) {
    super(Config.class);
    this.authAdapterRepository = authAdapterRepository;
    this.properties = properties;
    this.responseErrorHandler = errorInterceptor;
  }

  @Override
  public GatewayFilter apply(Config config) {
    return new OrderedGatewayFilter((exchange, chain) ->
        validateRequest(exchange)
            .then(chain.filter(exchange))
            .onErrorResume(Exception.class, exception -> responseErrorHandler.handleException(properties, exception, exchange))
        ,1);
  }

  Mono<Boolean> validateRequest(ServerWebExchange exchange) {
    Set<Boolean> status = new HashSet<>();
    return !properties.getAuthentication().isEnableAuth()
        ? Mono.just(Boolean.TRUE)
        : authAdapterRepository.getRoles(extractHeadersAsMap(exchange.getRequest()))
        .flatMapIterable(HashMap::keySet)
        .reduce(status, (currentStatus, role) -> {
          currentStatus.add(getExpectedRoles().contains(role));
          return currentStatus;
        })
        .map(setStatus -> setStatus.contains(Boolean.TRUE))
        .filter(Boolean.TRUE::equals)
        .switchIfEmpty(Mono.error(new AuthorizationException("ExpectedRoleNotFound", "The expected role was not found")))
        .doOnSuccess(success -> TokenValidatorUtil.validateAuthToken(exchange));
  }

  private List<String> getExpectedRoles() {
    String[] roles = properties.getAuthentication().getExpectedRoles().split(",");
    return new ArrayList<>(Arrays.asList(roles));
  }

  public static class Config {}
}
