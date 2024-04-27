package com.demo.bbq.application.helper;

import com.demo.bbq.utils.errors.exceptions.BusinessException;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Slf4j
@Component
@RequiredArgsConstructor
public class RequestValidatorHelper<T> {

  private final Validator validator;

  public void validateRequestBody(T requestBody) {
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
