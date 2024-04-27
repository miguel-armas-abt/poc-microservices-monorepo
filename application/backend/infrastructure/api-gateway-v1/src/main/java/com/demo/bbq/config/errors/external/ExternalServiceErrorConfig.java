package com.demo.bbq.config.errors.external;

import com.demo.bbq.utils.errors.external.DefaultClientErrorService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExternalServiceErrorConfig {

  @Bean
  public DefaultClientErrorService createExternalErrorServiceDefault() {
    return new DefaultClientErrorService();
  }
}
