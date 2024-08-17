package com.demo.bbq.commons.config;

import com.demo.bbq.commons.toolkit.validator.body.BodyValidator;
import com.demo.bbq.commons.toolkit.validator.headers.DefaultHeadersMapper;
import com.demo.bbq.commons.toolkit.validator.params.ParamMapper;
import com.demo.bbq.commons.toolkit.validator.params.ParamValidator;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.Validator;

@Configuration
public class ValidatorConfig {

  @Bean
  public BodyValidator requestValidator(Validator validator) {
    return new BodyValidator();
  }

  @Bean
  public DefaultHeadersMapper defaultHeadersMapper() {
    return new DefaultHeadersMapper();
  }

  @Bean
  public ParamValidator headerValidator(List<ParamMapper> paramMappers, BodyValidator bodyValidator) {
    return new ParamValidator(paramMappers, bodyValidator);
  }
}
