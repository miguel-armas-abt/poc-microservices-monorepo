package com.demo.bbq.infrastructure.authadapter.domain.exception;

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
public enum AuthAdapterException {

  ERROR0000(ApiExceptionType.AUTH_RULES, "JWT is not valid"),
  ERROR0001(ApiExceptionType.AUTH_RULES, "Unable to logout"),
  ERROR0002(ApiExceptionType.AUTH_RULES, "Unable to refresh"),
  ERROR0003(ApiExceptionType.AUTH_RULES, "Token is expired");

  private final ApiExceptionType type;
  private final String message;

  private final Supplier<String> generateErrorCode = () ->
      this.getType().getCode()
          .concat(CharacterConstant.DOT)
          .concat(this.name().substring(5));

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
