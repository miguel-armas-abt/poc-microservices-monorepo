package com.demo.poc.commons.core.validations;

import com.demo.poc.commons.core.errors.exceptions.InvalidFieldException;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class BodyValidator {

  private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  public <T> Single<T> validateAndGet(T body) {
    Set<ConstraintViolation<T>> violations = validator.validate(body);
    return handleValidationErrors(violations)
        .andThen(Single.just(body));
  }

  public <T> boolean isValid(T body) {
    return validator.validate(body).isEmpty();
  }

  private static <T> Completable handleValidationErrors(Set<ConstraintViolation<T>> violations) {
    if (!violations.isEmpty()) {
      String errorMessages = violations.stream()
          .map(violation -> String.format("The field '%s' %s",
              violation.getPropertyPath(), violation.getMessage()))
          .collect(Collectors.joining("; "));
      return Completable.error(new InvalidFieldException(errorMessages));
    }
    return Completable.complete();
  }
}