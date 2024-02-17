package com.demo.bbq.business.tableplacement.infrastructure.rest.common;

import com.demo.bbq.support.exception.model.ApiException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class RequestValidator<T> {

  private final Validator validator;

  public void validateRequest(T requestBody, Class<?> requestClass) {
    Errors errors = new BeanPropertyBindingResult(requestBody, requestClass.getName());
    validator.validate(requestBody, errors);
    if(errors.hasErrors()) {
      String errorMessage = errors.getFieldErrors()
          .stream()
          .map(field -> field.getField() + " " + field.getDefaultMessage() + "\n")
          .reduce(String::concat)
          .orElse(StringUtils.EMPTY);

      throw ApiException.builder()
          .status(HttpStatus.BAD_REQUEST)
          .message(errorMessage)
          .build();
    }
  }
}
