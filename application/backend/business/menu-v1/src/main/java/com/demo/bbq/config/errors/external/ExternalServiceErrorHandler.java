package com.demo.bbq.config.errors.external;

import com.demo.bbq.application.properties.ServiceConfigurationProperties;
import com.demo.bbq.utils.errors.external.RestClientErrorService;
import com.demo.bbq.utils.errors.matcher.ExternalErrorMatcherUtil;
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
    return ExternalErrorMatcherUtil.build(httpException, errorWrapperClass, serviceName, services, properties);
  }
}
