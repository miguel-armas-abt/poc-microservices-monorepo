package com.demo.poc.commons.errors.exceptions;

import com.demo.poc.commons.errors.dto.ErrorDTO;
import com.demo.poc.commons.errors.dto.ErrorType;
import lombok.Getter;

@Getter
public class SystemException extends RuntimeException {

  private final ErrorDTO errorDetail;

  public SystemException(String code) {
    super(code);
    this.errorDetail = ErrorDTO.builder()
        .type(ErrorType.SYSTEM)
        .code(code)
        .build();
  }

  public SystemException(String code, String message) {
    super(message);
    this.errorDetail = ErrorDTO.builder()
        .type(ErrorType.SYSTEM)
        .code(code)
        .message(message)
        .build();
  }
}
