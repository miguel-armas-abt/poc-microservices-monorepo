package com.demo.bbq.commons.config;

import com.demo.bbq.commons.errors.handler.external.strategy.DefaultErrorStrategy;
import com.demo.bbq.commons.errors.handler.response.ResponseErrorHandler;
import com.demo.bbq.commons.properties.ConfigurationBaseProperties;
import com.demo.bbq.commons.toolkit.serialization.JsonSerializer;
import com.demo.bbq.commons.tracing.logging.error.ErrorLogger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ErrorHandlerConfig {

  @Bean
  public DefaultErrorStrategy defaultErrorStrategy(JsonSerializer jsonSerializer) {
    return new DefaultErrorStrategy(jsonSerializer);
  }

  @Bean
  public ResponseErrorHandler responseErrorHandler(ErrorLogger errorLogger, ConfigurationBaseProperties properties) {
    return new ResponseErrorHandler(errorLogger, properties);
  }
}
