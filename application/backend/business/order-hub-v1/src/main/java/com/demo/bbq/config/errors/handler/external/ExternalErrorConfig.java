package com.demo.bbq.config.errors.handler.external;

import com.demo.bbq.config.properties.ServiceConfigurationProperties;
import com.demo.bbq.commons.errors.handler.external.ExternalErrorHandler;
import com.demo.bbq.commons.errors.handler.external.strategy.DefaultErrorStrategy;
import com.demo.bbq.commons.errors.handler.external.strategy.RestClientErrorStrategy;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExternalErrorConfig {

  @Bean
  public DefaultErrorStrategy defaultErrorStrategy() {
    return new DefaultErrorStrategy();
  }

  @Bean
  public ExternalErrorHandler externalErrorHandler(List<RestClientErrorStrategy> services,
                                                   ServiceConfigurationProperties properties) {
    return new ExternalErrorHandler(services, properties);
  }
}