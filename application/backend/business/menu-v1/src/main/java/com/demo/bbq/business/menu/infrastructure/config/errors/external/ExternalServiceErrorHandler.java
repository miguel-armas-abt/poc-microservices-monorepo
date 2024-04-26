package com.demo.bbq.business.menu.infrastructure.config.errors.external;

import com.demo.bbq.business.menu.application.properties.ServiceConfigurationProperties;
import com.demo.bbq.utils.errors.handler.external.ExternalClientErrorUtil;
import com.demo.bbq.utils.errors.handler.external.service.RestClientErrorService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;

@Component
@RequiredArgsConstructor
public class ExternalServiceErrorHandler {

  private final List<RestClientErrorService> services;
  private final ServiceConfigurationProperties properties;

  public Throwable handleError(HttpStatusCodeException httpException,
                               Class<?> errorWrapperClass,
                               String serviceName) {
    return ExternalClientErrorUtil.handleError(httpException, errorWrapperClass, serviceName, services, properties);
  }
}
