package com.demo.poc.commons.errors.exceptions;

import com.demo.poc.commons.errors.dto.ErrorDTO;
import com.demo.poc.commons.errors.dto.ErrorType;
import lombok.Getter;
import jakarta.ws.rs.core.Response.Status;

@Getter
public class ExternalServiceException extends RuntimeException {

  private final ErrorDTO errorDetail;
  private final Status httpStatusCode;

  public ExternalServiceException(String code, String message, ErrorType errorType, Status httpStatusCode) {
    super(message);
    this.errorDetail = ErrorDTO.builder()
        .type(errorType)
        .code(code)
        .message(message)
        .build();
    this.httpStatusCode = httpStatusCode;
  }
}
