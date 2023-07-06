package com.demo.bbq.business.menuoption.domain.exception;

import com.demo.bbq.support.constant.CharacterConstant;
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
public enum MenuOptionException {

  ERROR0000(ApiExceptionType.NO_DATA, "The menu option does not exist"),
  ERROR0001(ApiExceptionType.BUSINESS_RULES, "The menu option category is not defined");

  private final String serviceNumber = "bs.001";
  private final ApiExceptionType type;
  private final String message;

  private final Supplier<String> generateErrorCode = () ->
      this.getServiceNumber()
          .concat(CharacterConstant.DOT)
          .concat(this.getType().getCode())
          .concat(CharacterConstant.DOT)
          .concat(this.name().toLowerCase());

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
        .type(this.type.getDescription())
        .status(this.type.getHttpStatus());
  }
}
