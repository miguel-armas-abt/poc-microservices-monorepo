package com.demo.bbq.infrastructure.apigateway.domain.exception;

import com.demo.bbq.support.exception.util.ApiExceptionUtil;
import com.demo.bbq.support.exception.enums.ApiExceptionType;
import com.demo.bbq.support.exception.model.ApiException;
import com.demo.bbq.support.exception.model.builder.ApiExceptionBuilder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Getter
public enum ApiGatewayException {

  ERROR0000(ApiExceptionType.AUTH_RULES, "Missing  Authorization header"),
  ERROR0001(ApiExceptionType.AUTH_RULES, "Bad Authorization structure"),
  ERROR0002(ApiExceptionType.AUTH_RULES, "The expected role was not found"),
  ERROR0003(ApiExceptionType.TIMEOUT, "Could not connect to external service");

  private final String SERVICE_NAME = "api-gateway-v1";
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
        .errorCode(ApiExceptionUtil.buildErrorCode(SERVICE_NAME, this.name()))
        .message(this.message)
        .type(this.type.getDescription())
        .status(this.type.getHttpStatus());
  }

}
