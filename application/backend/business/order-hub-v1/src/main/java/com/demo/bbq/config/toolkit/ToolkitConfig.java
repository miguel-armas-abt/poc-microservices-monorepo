package com.demo.bbq.config.toolkit;

import com.demo.bbq.commons.toolkit.RequestValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.Validator;

@Configuration
public class ToolkitConfig {

  @Bean
  public RequestValidator requestValidator(Validator validator) {
    return new RequestValidator(validator);
  }
}