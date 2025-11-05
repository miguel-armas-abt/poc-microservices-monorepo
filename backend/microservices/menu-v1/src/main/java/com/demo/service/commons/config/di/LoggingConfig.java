package com.demo.service.commons.config.di;

import com.demo.commons.logging.ErrorThreadContextInjector;
import com.demo.commons.logging.ThreadContextInjector;
import com.demo.commons.properties.ConfigurationBaseProperties;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

@ApplicationScoped
public class LoggingConfig {

  @Produces
  public ThreadContextInjector threadContextInjector(ConfigurationBaseProperties properties) {
    return new ThreadContextInjector(properties);
  }

  @Produces
  public ErrorThreadContextInjector errorThreadContextInjector(ThreadContextInjector threadContextInjector) {
    return new ErrorThreadContextInjector(threadContextInjector);
  }

}