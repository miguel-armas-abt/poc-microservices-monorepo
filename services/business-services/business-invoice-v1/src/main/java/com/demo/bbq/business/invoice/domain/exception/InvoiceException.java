package com.demo.bbq.business.invoice.domain.exception;

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
public enum InvoiceException {

  ERROR0000(ApiExceptionType.NO_DATA, "The dining room table does not exist"),
  ERROR0001(ApiExceptionType.BUSINESS_RULES, "The requested menu order doesn't exist");

  private final String serviceNumber = "bs.003";
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

