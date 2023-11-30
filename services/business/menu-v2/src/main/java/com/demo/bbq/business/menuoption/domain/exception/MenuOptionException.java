package com.demo.bbq.business.menuoption.domain.exception;

import com.demo.bbq.business.menuoption.infrastructure.exception.catalog.ApiExceptionType;
import com.demo.bbq.business.menuoption.infrastructure.exception.model.ApiException;
import com.demo.bbq.business.menuoption.infrastructure.exception.model.builder.ApiExceptionBuilder;
import com.demo.bbq.business.menuoption.infrastructure.exception.util.ApiExceptionUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MenuOptionException {

  ERROR0000(ApiExceptionType.NO_DATA, "The menu option does not exist"),
  ERROR0001(ApiExceptionType.BUSINESS_RULES, "The menu category is not defined"),
  ERROR0002(ApiExceptionType.TIMEOUT, "Could not connect to external service");

  private final String SERVICE_NAME = "menu-v1";
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
