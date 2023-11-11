package com.demo.bbq.business.invoice.domain.exception;

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
public enum InvoiceException {

  ERROR0000(ApiExceptionType.BUSINESS_RULES, "The total amount is less than zero"),
  ERROR0001(ApiExceptionType.BUSINESS_RULES, "Payment status could not be updated");

  private final String SERVICE_NAME = "invoice-v1";
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

