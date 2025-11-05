package com.demo.service.commons.config.di;

import com.demo.commons.validations.BodyValidator;
import com.demo.commons.validations.ParamMapper;
import com.demo.commons.validations.ParamValidator;
import com.demo.commons.validations.headers.DefaultHeadersMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.Produces;
import jakarta.validation.Validator;

@ApplicationScoped
public class ValidatorConfig {

  @Produces
  public BodyValidator requestValidator(Validator validator) {
    return new BodyValidator(validator);
  }

  @Produces
  public DefaultHeadersMapper defaultHeadersMapper() {
    return new DefaultHeadersMapper();
  }

  @Produces
  public ParamValidator paramValidator(Instance<ParamMapper> mappers, BodyValidator bodyValidator) {
    return new ParamValidator(mappers, bodyValidator);
  }
}