package com.demo.poc.config.tracing.logging;

import com.demo.poc.commons.tracing.logging.RestClientRequestLogger;
import com.demo.poc.commons.tracing.logging.RestClientResponseLogger;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

@ApplicationScoped
public class LoggingConfig {

  @Produces
  public RestClientRequestLogger restClientRequestLogger() {
    return new RestClientRequestLogger();
  }

  @Produces
  public RestClientResponseLogger restClientResponseLogger() {
    return new RestClientResponseLogger();
  }
}