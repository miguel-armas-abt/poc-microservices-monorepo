package com.demo.bbq.config.tracing.logging;

import com.demo.bbq.utils.tracing.logging.RestClientRequestLogger;
import com.demo.bbq.utils.tracing.logging.RestClientResponseLogger;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

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