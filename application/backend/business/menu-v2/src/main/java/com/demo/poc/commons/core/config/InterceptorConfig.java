package com.demo.poc.commons.core.config;

import com.demo.poc.commons.core.interceptor.error.ConstraintErrorInterceptor;
import com.demo.poc.commons.core.interceptor.error.ErrorInterceptor;
import com.demo.poc.commons.core.interceptor.restclient.RestClientRequestInterceptor;
import com.demo.poc.commons.core.interceptor.restclient.RestClientResponseInterceptor;
import com.demo.poc.commons.core.logging.ThreadContextInjector;
import com.demo.poc.commons.custom.properties.ApplicationProperties;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

@ApplicationScoped
public class InterceptorConfig {

  @Produces
  public RestClientRequestInterceptor restClientRequestInterceptor(ThreadContextInjector threadContextInjector) {
    return new RestClientRequestInterceptor(threadContextInjector);
  }

  @Produces
  public RestClientResponseInterceptor restClientResponseInterceptor(ThreadContextInjector threadContextInjector) {
    return new RestClientResponseInterceptor(threadContextInjector);
  }

  @Produces
  public ErrorInterceptor errorInterceptor(ApplicationProperties properties,
                                           ThreadContextInjector threadContextInjector) {
    return new ErrorInterceptor(properties, threadContextInjector);
  }

  @Produces
  public ConstraintErrorInterceptor constraintErrorInterceptor(ApplicationProperties properties,
                                                               ThreadContextInjector threadContextInjector) {
    return new ConstraintErrorInterceptor(properties, threadContextInjector);
  }
}