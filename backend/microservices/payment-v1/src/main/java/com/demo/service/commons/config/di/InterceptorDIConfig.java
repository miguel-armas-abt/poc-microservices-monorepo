package com.demo.service.commons.config.di;

import com.demo.commons.errors.selector.ResponseErrorSelector;
import com.demo.commons.interceptor.error.ErrorInterceptor;
import com.demo.commons.logging.ErrorThreadContextInjector;
import com.demo.service.commons.properties.ApplicationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InterceptorDIConfig {

  @Bean
  public ErrorInterceptor errorInterceptor(ErrorThreadContextInjector contextInjector,
                                           ApplicationProperties properties,
                                           ResponseErrorSelector responseErrorSelector) {
    return new ErrorInterceptor(contextInjector, properties, responseErrorSelector);
  }
}
