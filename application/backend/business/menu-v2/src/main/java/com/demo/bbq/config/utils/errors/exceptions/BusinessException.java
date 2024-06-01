package com.demo.bbq.config.utils.errors.exceptions;

import com.demo.bbq.config.utils.errors.dto.ErrorDTO;
import com.demo.bbq.config.utils.errors.dto.ErrorType;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

  private final ErrorDTO errorDetail;

  public BusinessException(String code) {
    super(code);
    this.errorDetail = ErrorDTO.builder()
        .type(ErrorType.BUSINESS)
        .code(code)
        .build();
  }

  public BusinessException(String code, String message) {
    super(message);
    this.errorDetail = ErrorDTO.builder()
        .type(ErrorType.BUSINESS)
        .code(code)
        .message(message)
        .build();
  }
}
