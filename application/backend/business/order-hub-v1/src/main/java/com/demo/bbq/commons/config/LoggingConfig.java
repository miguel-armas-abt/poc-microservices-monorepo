package com.demo.bbq.commons.config;

import com.demo.bbq.commons.core.logging.ThreadContextInjector;
import com.demo.bbq.commons.core.properties.ConfigurationBaseProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoggingConfig {

  @Bean
  public ThreadContextInjector threadContextInjector(ConfigurationBaseProperties properties) {
    return new ThreadContextInjector(properties);
  }
}
