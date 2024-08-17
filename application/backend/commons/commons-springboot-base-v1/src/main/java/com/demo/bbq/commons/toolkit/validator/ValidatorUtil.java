package com.demo.bbq.commons.toolkit.validator;

import com.demo.bbq.commons.errors.exceptions.BusinessException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.constraints.NotNull;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidatorUtil {

  public static  <T> void handleValidationErrors(@NotNull Set<ConstraintViolation<T>> violations) {
    if (!violations.isEmpty()) {
      String errorMessages = violations.stream()
          .map(violation -> String.format("The field '%s' %s",
              violation.getPropertyPath(), violation.getMessage()))
          .collect(Collectors.joining("; "));
      throw new BusinessException("FieldValidationError", errorMessages);
    }
  }
}