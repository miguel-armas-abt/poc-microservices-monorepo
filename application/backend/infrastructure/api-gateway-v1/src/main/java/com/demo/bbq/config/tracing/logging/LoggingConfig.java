package com.demo.bbq.config.tracing.logging;

import com.demo.bbq.config.properties.ApplicationProperties;
import com.demo.bbq.commons.tracing.logging.RestClientRequestLogger;
import com.demo.bbq.commons.tracing.logging.RestClientResponseLogger;
import com.demo.bbq.commons.tracing.logging.RestServerLogger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.server.WebFilter;

@Configuration
public class LoggingConfig {

  @Bean
  public WebFilter restServerLogger(ApplicationProperties properties) {
    return new RestServerLogger(properties);
  }

  @Bean
  public ExchangeFilterFunction restClientRequestLogger(ApplicationProperties properties) {
    return new RestClientRequestLogger(properties);
  }

  @Bean
  public ExchangeFilterFunction restClientResponseLogger(ApplicationProperties properties) {
    return new RestClientResponseLogger(properties);
  }

}
