package com.demo.bbq.config.utils.errors.exceptions;

import com.demo.bbq.config.utils.errors.dto.ErrorDTO;
import com.demo.bbq.config.utils.errors.dto.ErrorType;
import lombok.Getter;
import javax.ws.rs.core.Response.Status;

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
