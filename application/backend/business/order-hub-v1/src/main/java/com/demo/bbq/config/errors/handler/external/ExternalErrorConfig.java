package com.demo.bbq.config.errors.handler.external;

import com.demo.bbq.utils.errors.handler.external.strategy.DefaultErrorStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExternalErrorConfig {

  @Bean
  public DefaultErrorStrategy createDefaultErrorStrategy() {
    return new DefaultErrorStrategy();
  }
}
