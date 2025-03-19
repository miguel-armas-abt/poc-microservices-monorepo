package com.demo.bbq.commons.config;

import com.demo.bbq.commons.errors.handler.external.strategy.DefaultErrorStrategy;
import com.demo.bbq.commons.interceptor.error.ErrorInterceptor;
import com.demo.bbq.commons.logging.ThreadContextInjector;
import com.demo.bbq.commons.properties.base.ConfigurationBaseProperties;
import com.demo.bbq.commons.serialization.JsonSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ErrorConfig {

  @Bean
  public DefaultErrorStrategy defaultErrorStrategy(JsonSerializer jsonSerializer) {
    return new DefaultErrorStrategy(jsonSerializer);
  }

  @Bean
  public ErrorInterceptor errorInterceptor(ThreadContextInjector threadContextInjector, ConfigurationBaseProperties properties) {
    return new ErrorInterceptor(threadContextInjector, properties);
  }
}
