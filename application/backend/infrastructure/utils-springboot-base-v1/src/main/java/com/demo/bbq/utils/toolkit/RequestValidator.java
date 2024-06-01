package com.demo.bbq.utils.toolkit;

import com.demo.bbq.utils.errors.exceptions.BusinessException;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@RequiredArgsConstructor
public class RequestValidator {

  private final Validator validator;

  public <T> void validateRequestBody(T requestBody) {
    validateObjectRecursively(requestBody);
  }

  private void validateObjectRecursively(Object obj) {
    Errors errors = new BeanPropertyBindingResult(obj, obj.getClass().getName());
    validator.validate(obj, errors);
    if (errors.hasErrors()) {
      throw throwInvalidRequest.apply(buildErrorMessage.apply(errors));
    }

    if (obj != null) {
      Class<?> objClass = obj.getClass();
      for (java.lang.reflect.Field field : objClass.getDeclaredFields()) {
        field.setAccessible(true);
        try {
          Object fieldValue = field.get(obj);
          if (fieldValue != null && !field.getType().isPrimitive() && !field.getType().getName().startsWith("java.")) {
            validateObjectRecursively(fieldValue);
          }
        } catch (IllegalAccessException e) {
          throw throwInvalidRequest.apply(e.getMessage());
        }
      }
    }
  }

  private static final Function<String, BusinessException> throwInvalidRequest = message ->
      new BusinessException("InvalidRequestBody", message);

  private static final Function<Errors, String> buildErrorMessage = errors ->
      errors.getFieldErrors()
          .stream()
          .map(fieldError -> fieldError.getField() + " " + fieldError.getDefaultMessage())
          .reduce((errorMessage, newErrorMessage) -> StringUtils.isEmpty(newErrorMessage) ? errorMessage : errorMessage + ", " + newErrorMessage)
          .orElse(StringUtils.EMPTY);
}
