package com.demo.bbq.infrastructure.authadapter.domain.exception;

import com.demo.bbq.support.exception.util.ApiExceptionUtil;
import com.demo.bbq.support.exception.catalog.ApiExceptionType;
import com.demo.bbq.support.exception.model.ApiException;
import com.demo.bbq.support.exception.model.builder.ApiExceptionBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Getter
public enum AuthAdapterException {

  ERROR0000(ApiExceptionType.AUTH_RULES, "JWT is not valid"),
  ERROR0001(ApiExceptionType.AUTH_RULES, "Unable to logout"),
  ERROR0002(ApiExceptionType.AUTH_RULES, "Unable to refresh"),
  ERROR0003(ApiExceptionType.AUTH_RULES, "Token is expired"),
  ERROR0004(ApiExceptionType.AUTH_RULES, "Could not connect to external service");

  private final String SERVICE_NAME = "auth-adapter-v1";
  private final ApiExceptionType type;
  private final String message;

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
        .errorCode(ApiExceptionUtil.generateErrorCode(type, SERVICE_NAME, this.name()))
        .message(this.message)
        .type(this.type.getDescription())
        .status(this.type.getHttpStatus());
  }

}
