package com.demo.bbq.infrastructure.authadapter.util.exception;

import com.demo.bbq.support.exception.catalog.ApiExceptionType;
import com.demo.bbq.support.exception.model.ApiException;
import com.demo.bbq.support.exception.model.builder.ApiExceptionBuilder;
import java.util.function.Supplier;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Getter
public enum AuthAdapterException {

  ERROR0000(ApiExceptionType.AUTH_RULES, "Token validation error"),
  ERROR0001(ApiExceptionType.AUTH_RULES, "Token validation error"),
  ERROR0002(ApiExceptionType.AUTH_RULES, "Unable to logout"),
  ERROR0003(ApiExceptionType.AUTH_RULES, "Unable to refresh");

  private final String serviceNumber = "in.002";
  private final ApiExceptionType type;
  private final String message;

  private final Supplier<String> generateErrorCode = () ->
      this.getServiceNumber().concat(this.getType().getCode()).concat(this.name());

  public ApiException buildException(Throwable cause) {
    return buildApiException()
        .cause(cause)
        .build();
  }

  public ApiException buildException() {
    return buildApiException()
        .build();
  }

  private ApiExceptionBuilder buildApiException() {
    return ApiException.builder()
        .errorCode(this.generateErrorCode.get())
        .message(this.message)
        .status(this.type.getHttpStatus());
  }

}
