package com.demo.bbq.commons.core.config;

import com.demo.bbq.commons.core.errors.external.strategy.DefaultErrorStrategy;
import com.demo.bbq.commons.core.serialization.JsonSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ErrorConfig {

  @Bean
  public DefaultErrorStrategy defaultErrorStrategy(JsonSerializer jsonSerializer) {
    return new DefaultErrorStrategy(jsonSerializer);
  }
}
