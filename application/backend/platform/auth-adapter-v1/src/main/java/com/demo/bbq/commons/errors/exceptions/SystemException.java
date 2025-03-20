package com.demo.bbq.commons.errors.exceptions;

import com.demo.bbq.commons.errors.dto.ErrorDTO;
import com.demo.bbq.commons.errors.dto.ErrorType;
import lombok.Getter;

import java.io.Serial;

@Getter
public class SystemException extends RuntimeException {

  @Serial
  private static final long serialVersionUID = -4701249853319723248L;

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
