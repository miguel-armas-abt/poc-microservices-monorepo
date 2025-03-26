package com.demo.bbq.commons.core.config;

import com.demo.bbq.commons.core.errors.handler.external.ExternalErrorHandler;
import com.demo.bbq.commons.core.errors.handler.external.strategy.DefaultErrorStrategy;
import com.demo.bbq.commons.core.errors.handler.external.strategy.RestClientErrorStrategy;
import com.demo.bbq.commons.core.serialization.JsonSerializer;
import com.demo.bbq.commons.custom.properties.ApplicationProperties;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ErrorConfig {

  @Bean
  public DefaultErrorStrategy defaultErrorStrategy(JsonSerializer jsonSerializer) {
    return new DefaultErrorStrategy(jsonSerializer);
  }

  @Bean
  public ExternalErrorHandler externalErrorHandler(List<RestClientErrorStrategy> services,
                                                   ApplicationProperties properties) {
    return new ExternalErrorHandler(services, properties);
  }
}
