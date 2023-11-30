package com.demo.bbq.support.exception.model.builder;

import com.demo.bbq.support.exception.model.ApiExceptionDetail;
import java.util.Optional;
import java.util.function.Function;
import org.apache.commons.lang3.StringUtils;

public class ApiExceptionDetailBuilder {

  private String message;
  private String errorCode;
  private final ApiExceptionBuilder builder;

  public ApiExceptionDetailBuilder(ApiExceptionBuilder builder) {
    this.builder = builder;
  }

  public ApiExceptionDetailBuilder withMessage(String message) {
    toOptional.apply(message).ifPresent(actualMessage -> this.message = actualMessage);
    return this;
  }

  public ApiExceptionDetailBuilder withErrorCode(String errorCode) {
    toOptional.apply(errorCode).ifPresent(actualErrorCode -> this.errorCode = actualErrorCode);
    return this;
  }

  public ApiExceptionBuilder push() {
    if (StringUtils.isNotBlank(this.errorCode) || StringUtils.isNotBlank(this.message)) {
      this.builder.getDetails().add(new ApiExceptionDetail(this.errorCode, this.message));
      this.errorCode = null;
      this.message = null;
    }
    return this.builder;
  }

  private final Function<String, Optional<String>> toOptional = field ->
      Optional.ofNullable(field).filter(StringUtils::isNotBlank);

}
