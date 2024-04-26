package com.demo.bbq.infrastructure.apigateway.infrastructure.config.errors.handler.external;

import com.demo.bbq.infrastructure.apigateway.application.properties.ServiceConfigurationProperties;
import com.demo.bbq.utils.errors.handler.external.ExternalClientErrorUtil;
import com.demo.bbq.utils.errors.handler.external.service.WebfluxClientErrorService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ExternalServiceErrorHandler {

  private final List<WebfluxClientErrorService> services;
  private final ServiceConfigurationProperties properties;

  public Mono<? extends Throwable> handleError(ClientResponse clientResponse,
                                               Class<?> errorWrapperClass,
                                               String serviceName) {
    return ExternalClientErrorUtil.handleError(clientResponse, errorWrapperClass, serviceName, services, properties);
  }
}
