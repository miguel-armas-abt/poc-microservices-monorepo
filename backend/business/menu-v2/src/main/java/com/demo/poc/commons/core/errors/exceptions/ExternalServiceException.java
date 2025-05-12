package com.demo.poc.commons.core.errors.exceptions;

import com.demo.poc.commons.core.errors.dto.ErrorDto;
import com.demo.poc.commons.core.errors.dto.ErrorType;
import jakarta.ws.rs.core.Response.Status;
import lombok.Getter;

@Getter
public class ExternalServiceException extends RuntimeException {

  private final ErrorDto errorDetail;
  private final Status httpStatusCode;

  public ExternalServiceException(String code, String message, ErrorType errorType, Status httpStatusCode) {
    super(message);
    this.errorDetail = ErrorDto.builder()
        .type(errorType)
        .code(code)
        .message(message)
        .build();
    this.httpStatusCode = httpStatusCode;
  }
}
