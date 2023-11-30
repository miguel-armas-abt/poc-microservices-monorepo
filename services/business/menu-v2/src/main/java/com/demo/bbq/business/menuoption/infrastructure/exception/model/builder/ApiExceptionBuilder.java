package com.demo.bbq.business.menuoption.infrastructure.exception.model.builder;

import com.demo.bbq.business.menuoption.infrastructure.exception.constant.ApiExceptionConstant;
import com.demo.bbq.business.menuoption.infrastructure.exception.model.ApiException;
import io.netty.handler.codec.http.HttpResponseStatus;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

public class ApiExceptionBuilder {

  private String message;
  private Throwable cause;
  private String type;
  private String errorCode;
  private HttpResponseStatus status;

  public ApiExceptionBuilder message(String message) {
    toOptional.apply(message).ifPresent(actualMessage -> this.message = actualMessage);
    return this;
  }

  public ApiExceptionBuilder cause(Throwable cause) {
    this.cause = cause;
    return this;
  }

  public ApiExceptionBuilder type(String type) {
    toOptional.apply(type).ifPresent(actualType -> this.type = actualType);
    return this;
  }

  public ApiExceptionBuilder errorCode(String errorCode) {
    toOptional.apply(errorCode)
        .filter(error -> error.matches(ApiExceptionConstant.ERROR_CODE_REGEX))
        .ifPresent(actualErrorCode -> this.errorCode = actualErrorCode);
    return this;
  }

  public ApiExceptionBuilder status(HttpResponseStatus status) {
    Optional.ofNullable(status).ifPresent(actualStatus -> this.status = actualStatus);
    return this;
  }

  public ApiException build() {
    if (Objects.nonNull(this.cause)) {
      this.cause(null);
    }
    return new ApiException(this.message, this.cause, this.type, this.errorCode, this.status);
  }

  private final Function<String, Optional<String>> toOptional = field ->
      Optional.ofNullable(field).filter(actualField -> !actualField.isEmpty());

}
