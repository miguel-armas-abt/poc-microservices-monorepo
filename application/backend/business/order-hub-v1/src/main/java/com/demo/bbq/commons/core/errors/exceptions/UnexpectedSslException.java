package com.demo.bbq.commons.core.errors.exceptions;

import com.demo.bbq.commons.core.errors.dto.ErrorDTO;
import com.demo.bbq.commons.core.errors.enums.ErrorDictionary;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UnexpectedSslException extends GenericException {

  private static final ErrorDictionary EXCEPTION = ErrorDictionary.UNEXPECTED_SSL_EXCEPTION;

  public UnexpectedSslException() {
    super(EXCEPTION.getMessage());
    this.httpStatus = HttpStatus.BAD_REQUEST;
    this.errorDetail = ErrorDTO.builder()
        .code(EXCEPTION.getCode())
        .message(EXCEPTION.getMessage())
        .build();
  }
}