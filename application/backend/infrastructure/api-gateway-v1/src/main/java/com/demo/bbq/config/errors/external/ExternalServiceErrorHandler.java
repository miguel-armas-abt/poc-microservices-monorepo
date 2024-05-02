package com.demo.bbq.config.errors.external;

import com.demo.bbq.config.properties.ServiceConfigurationProperties;
import com.demo.bbq.utils.errors.external.RestClientErrorService;
import com.demo.bbq.utils.errors.matcher.ExternalErrorMatcherUtil;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ExternalServiceErrorHandler {

  private final List<RestClientErrorService> services;
  private final ServiceConfigurationProperties properties;

  public Mono<? extends Throwable> handleError(ClientResponse clientResponse,
                                               Class<?> errorWrapperClass,
                                               String serviceName) {
    return ExternalErrorMatcherUtil.build(clientResponse, errorWrapperClass, serviceName, services, properties);
  }
}
