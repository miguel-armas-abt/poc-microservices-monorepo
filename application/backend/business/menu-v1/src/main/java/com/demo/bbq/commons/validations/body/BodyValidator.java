package com.demo.bbq.commons.validations.body;

import com.demo.bbq.commons.validations.utils.ValidatorUtil;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@RequiredArgsConstructor
public class BodyValidator {

  private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  public <T> void validate(T body) {
    Set<ConstraintViolation<T>> violations = validator.validate(body);
    ValidatorUtil.handleValidationErrors(violations);
  }

  public <T> boolean isValid(T body) {
    return validator.validate(body).isEmpty();
  }
}