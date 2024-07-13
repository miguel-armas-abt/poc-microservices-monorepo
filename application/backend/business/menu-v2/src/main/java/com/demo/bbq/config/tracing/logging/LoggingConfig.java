package com.demo.bbq.config.tracing.logging;

import com.demo.bbq.commons.tracing.logging.RestClientRequestLogger;
import com.demo.bbq.commons.tracing.logging.RestClientResponseLogger;
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