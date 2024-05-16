package com.demo.bbq.config.errors.external;

import com.demo.bbq.config.properties.ServiceConfigurationProperties;
import com.demo.bbq.utils.errors.external.ExternalClientErrorWrapper;
import com.demo.bbq.utils.errors.external.ExternalErrorUtil;
import com.demo.bbq.utils.errors.external.RestClientErrorService;
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
                               Class<? extends ExternalClientErrorWrapper> errorWrapperClass,
                               String serviceName) {
    return ExternalErrorUtil.build(httpException, errorWrapperClass, serviceName, services, properties);
  }
}
