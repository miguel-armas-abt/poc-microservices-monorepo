package com.demo.bbq.business.menuoption.infrastructure.exception.model;

import com.demo.bbq.business.menuoption.infrastructure.exception.model.builder.ApiExceptionBuilder;
import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiException extends RuntimeException {

  private String message;
  private Throwable cause;
  private String type;
  private String errorCode;
  private HttpResponseStatus httpStatus;

  public ApiException(String message, Throwable cause, String type, String errorCode, HttpResponseStatus httpStatus) {
    super(message, cause);
    this.type = type;
    this.message = message;
    this.errorCode = errorCode;
    this.httpStatus = httpStatus;
  }

  public static ApiExceptionBuilder builder() {
    return new ApiExceptionBuilder();
  }
}
