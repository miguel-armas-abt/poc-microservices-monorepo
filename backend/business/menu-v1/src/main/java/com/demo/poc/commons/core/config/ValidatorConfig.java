package com.demo.poc.commons.core.config;

import com.demo.poc.commons.core.validations.BodyValidator;
import com.demo.poc.commons.core.validations.ParamMapper;
import com.demo.poc.commons.core.validations.ParamValidator;
import com.demo.poc.commons.core.validations.headers.DefaultHeadersMapper;
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