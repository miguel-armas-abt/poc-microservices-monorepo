package com.demo.bbq.config.errors.handler.external;

import com.demo.bbq.config.properties.ServiceConfigurationProperties;
import com.demo.bbq.utils.errors.handler.external.ExternalErrorHandlerUtil;
import com.demo.bbq.utils.errors.handler.external.strategy.ExternalErrorWrapper;
import com.demo.bbq.utils.errors.handler.external.strategy.RestClientErrorStrategy;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;

@Component
@RequiredArgsConstructor
public class ExternalErrorHandler {

  private final List<RestClientErrorStrategy> services;
  private final ServiceConfigurationProperties properties;

  public Throwable handleError(HttpStatusCodeException httpException,
                               Class<? extends ExternalErrorWrapper> errorWrapperClass,
                               String serviceName) {
    return ExternalErrorHandlerUtil.build(httpException, errorWrapperClass, serviceName, services, properties);
  }
}
