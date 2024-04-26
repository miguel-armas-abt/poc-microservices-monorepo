package com.demo.bbq.utils.errors.exceptions;

import com.demo.bbq.utils.errors.dto.ErrorDTO;
import com.demo.bbq.utils.errors.dto.ErrorType;
import java.io.Serial;
import lombok.Getter;
import org.springframework.http.HttpStatusCode;

@Getter
public class ExternalServiceException extends RuntimeException {

  @Serial
  private static final long serialVersionUID = 480700693894159856L;

  private final ErrorDTO errorDetail;
  private final HttpStatusCode httpStatusCode;

  public ExternalServiceException(String code, String message, HttpStatusCode httpStatusCode) {
    super(message);
    this.errorDetail = ErrorDTO.builder()
        .type(ErrorType.EXTERNAL)
        .code(code)
        .message(message)
        .build();
    this.httpStatusCode = httpStatusCode;
  }
}
