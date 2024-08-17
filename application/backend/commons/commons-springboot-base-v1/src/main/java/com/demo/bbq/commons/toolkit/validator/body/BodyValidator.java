package com.demo.bbq.commons.toolkit.validator.body;

import static com.demo.bbq.commons.toolkit.validator.ValidatorUtil.handleValidationErrors;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.Set;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BodyValidator {

  private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  public <T> void validate(T body) {
    Set<ConstraintViolation<T>> violations = validator.validate(body);
    handleValidationErrors(violations);
  }

  public <T> boolean isValid(T body) {
    return validator.validate(body).isEmpty();
  }
}