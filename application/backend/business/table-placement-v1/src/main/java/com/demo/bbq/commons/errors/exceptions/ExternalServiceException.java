package com.demo.bbq.commons.errors.exceptions;

import com.demo.bbq.commons.errors.dto.ErrorDTO;
import com.demo.bbq.commons.errors.dto.ErrorType;
import lombok.Getter;
import org.springframework.http.HttpStatusCode;

import java.io.Serial;

@Getter
public class ExternalServiceException extends RuntimeException {

  @Serial
  private static final long serialVersionUID = 480700693894159856L;

  private final ErrorDTO errorDetail;
  private final HttpStatusCode httpStatusCode;

  public ExternalServiceException(String code, String message, ErrorType errorType, HttpStatusCode httpStatusCode) {
    super(message);
    this.errorDetail = ErrorDTO.builder()
        .type(errorType)
        .code(code)
        .message(message)
        .build();
    this.httpStatusCode = httpStatusCode;
  }
}
