package com.demo.bbq.config.errors.handler.external;

import org.antlr.v4.runtime.DefaultErrorStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExternalErrorConfig {

  @Bean
  public DefaultErrorStrategy createDefaultErrorStrategy() {
    return new DefaultErrorStrategy();
  }
}
