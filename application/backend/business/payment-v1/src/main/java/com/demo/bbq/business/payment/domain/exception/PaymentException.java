package com.demo.bbq.business.payment.domain.exception;

import com.demo.bbq.support.exception.util.ApiExceptionUtil;
import com.demo.bbq.support.exception.enums.ApiExceptionType;
import com.demo.bbq.support.exception.model.ApiException;
import com.demo.bbq.support.exception.model.builder.ApiExceptionBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Getter
public enum PaymentException {

  ERROR1000(ApiExceptionType.NO_DATA, "Generic error");

  private final String SERVICE_NAME = "payment-v1";
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
