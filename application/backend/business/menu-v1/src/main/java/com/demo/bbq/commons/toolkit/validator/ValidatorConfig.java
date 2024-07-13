package com.demo.bbq.commons.toolkit.validator;

import com.demo.bbq.commons.toolkit.validator.header.HeaderValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.Validator;

@Configuration
public class ValidatorConfig {

  @Bean
  public RequestValidator requestValidator(Validator validator) {
    return new RequestValidator(validator);
  }

  @Bean
  public HeaderValidator headerValidator(RequestValidator requestValidator) {
    return new HeaderValidator(requestValidator);
  }
}