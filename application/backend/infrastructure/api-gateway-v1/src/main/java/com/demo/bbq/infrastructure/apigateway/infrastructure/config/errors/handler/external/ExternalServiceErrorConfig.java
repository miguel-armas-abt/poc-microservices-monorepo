package com.demo.bbq.infrastructure.apigateway.infrastructure.config.errors.handler.external;

import com.demo.bbq.utils.errors.handler.external.service.DefaultClientErrorService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExternalServiceErrorConfig {

  @Bean
  public DefaultClientErrorService createExternalErrorServiceDefault() {
    return new DefaultClientErrorService();
  }
}
