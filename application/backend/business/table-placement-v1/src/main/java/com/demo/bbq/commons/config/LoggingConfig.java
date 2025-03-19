package com.demo.bbq.commons.config;

import com.demo.bbq.commons.properties.base.ConfigurationBaseProperties;
import com.demo.bbq.commons.logging.ThreadContextInjector;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoggingConfig {

  @Bean
  public ThreadContextInjector threadContextInjector(ConfigurationBaseProperties properties) {
    return new ThreadContextInjector(properties);
  }
}
