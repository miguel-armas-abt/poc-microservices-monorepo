package com.demo.poc.commons.core.errors.exceptions;

import com.demo.poc.commons.core.errors.dto.ErrorDTO;
import com.demo.poc.commons.core.errors.dto.ErrorType;
import jakarta.ws.rs.core.Response.Status;
import lombok.Getter;

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
