package com.demo.poc.commons.core.validations;

import com.demo.poc.commons.core.constants.Symbol;
import com.demo.poc.commons.core.errors.exceptions.InvalidFieldException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class BodyValidator {

  private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  public <T> Mono<T> validateAndGet(T body) {
    Set<ConstraintViolation<T>> violations = validator.validate(body);
    return handleValidationErrors(violations).thenReturn(body);
  }

  public <T> boolean isValid(T body) {
    return validator.validate(body).isEmpty();
  }

  private <T> Mono<Void> handleValidationErrors(Set<ConstraintViolation<T>> violations) {
    if (!violations.isEmpty()) {
      String errorMessages = violations.stream()
          .map(violation -> String.format("The value of %s %s",
              violation.getPropertyPath(), violation.getMessage()))
          .collect(Collectors.joining(Symbol.COMMA));
      return Mono.error(new InvalidFieldException(errorMessages));
    }
    return Mono.empty();
  }
}