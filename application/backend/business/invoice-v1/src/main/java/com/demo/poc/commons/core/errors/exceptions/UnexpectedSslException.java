package com.demo.poc.commons.core.errors.exceptions;

import com.demo.poc.commons.core.errors.dto.ErrorDTO;
import com.demo.poc.commons.core.errors.enums.ErrorDictionary;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UnexpectedSslException extends GenericException {

  private static final ErrorDictionary EXCEPTION = ErrorDictionary.UNEXPECTED_SSL_EXCEPTION;

  public UnexpectedSslException(String message) {
    super(message);
    this.httpStatus = HttpStatus.BAD_REQUEST;
    this.errorDetail = ErrorDTO.builder()
        .code(EXCEPTION.getCode())
        .message(EXCEPTION.getMessage())
        .build();
  }
}