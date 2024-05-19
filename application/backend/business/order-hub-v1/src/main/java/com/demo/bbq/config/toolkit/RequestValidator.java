package com.demo.bbq.config.toolkit;

import com.demo.bbq.utils.toolkit.RequestValidatorUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Validator;

@Slf4j
@Component
@RequiredArgsConstructor
public class RequestValidator<T> {

  private final Validator validator;

  public void validateRequestBody(T requestBody) {
    RequestValidatorUtil.validateRequestBody(requestBody, validator);
  }
}