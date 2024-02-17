package com.demo.bbq.business.tableplacement.domain.exception;

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
public enum TablePlacementExceptionEnum {

  ERROR0000(ApiExceptionType.NO_DATA, "The table does not exist");

  private final String SERVICE_NAME = "table_placement-v1";
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

