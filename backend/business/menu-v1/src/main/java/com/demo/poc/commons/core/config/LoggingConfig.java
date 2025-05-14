package com.demo.poc.commons.core.config;

import com.demo.poc.commons.core.logging.ErrorThreadContextInjector;
import com.demo.poc.commons.core.logging.ThreadContextInjector;
import com.demo.poc.commons.custom.properties.ApplicationProperties;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

@ApplicationScoped
public class LoggingConfig {

  @Produces
  public ThreadContextInjector threadContextInjector(ApplicationProperties properties) {
    return new ThreadContextInjector(properties);
  }

  @Produces
  public ErrorThreadContextInjector errorThreadContextInjector(ThreadContextInjector threadContextInjector) {
    return new ErrorThreadContextInjector(threadContextInjector);
  }

}