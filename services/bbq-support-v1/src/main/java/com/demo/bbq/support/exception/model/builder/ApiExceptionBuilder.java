package com.demo.bbq.support.exception.model.builder;

import com.demo.bbq.support.exception.constant.ApiExceptionConstant;
import com.demo.bbq.support.exception.model.ApiExceptionDetail;
import com.demo.bbq.support.exception.model.ApiException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

public class ApiExceptionBuilder {

  private String message;
  private Throwable cause;
  private String type;
  private String errorCode;
  private HttpStatus status;
  private final List<ApiExceptionDetail> details = new ArrayList<>();

  public ApiExceptionBuilder message(String message) {
    toOptional.apply(message).ifPresent(actualMessage -> this.message = actualMessage);
    return this;
  }

  public ApiExceptionBuilder cause(Throwable cause) {
    this.cause = cause;
    return this;
  }

  public ApiExceptionBuilder type(String type) {
    toOptional.apply(type).ifPresent(actualType -> this.type = actualType);
    return this;
  }

  public ApiExceptionBuilder errorCode(String errorCode) {
    toOptional.apply(errorCode)
        .filter(error -> error.matches(ApiExceptionConstant.ERROR_CODE_REGEX))
        .ifPresent(actualErrorCode -> this.errorCode = actualErrorCode);
    return this;
  }

  public ApiExceptionBuilder status(HttpStatus status) {
    Optional.ofNullable(status).ifPresent(actualStatus -> this.status = actualStatus);
    return this;
  }

  public ApiExceptionDetailBuilder addDetail() {
    return new ApiExceptionDetailBuilder(this);
  }

  public List<ApiExceptionDetail> getDetails() {
    return this.details;
  }

  public ApiException build() {
    if (Objects.nonNull(this.cause)) {
      if (this.cause instanceof ApiException) {
        addDetailFromPreviousApiException.accept((ApiException) this.cause);
      } else {
        addDetailFromPreviousOtherException.accept(this.cause);
      }
      this.cause(null);
    }
    return new ApiException(this.message, this.cause, this.type, this.errorCode, this.status, new ArrayList<>(this.details));
  }

  private final Function<String, Optional<String>> toOptional = field ->
      Optional.ofNullable(field).filter(StringUtils::isNotBlank);

  private final Consumer<ApiException> addDetailFromPreviousApiException = previousException -> {
    this.addDetail()
        .withErrorCode(previousException.getErrorCode())
        .withMessage(previousException.getMessage())
        .push();

    previousException.getDetails()
        .forEach(detail -> this.addDetail()
            .withErrorCode(detail.getErrorCode())
            .withMessage(detail.getMessage())
            .push());
  };

  private final Consumer<Throwable> addDetailFromPreviousOtherException = previousException -> this.addDetail()
      .withMessage(previousException.getClass().getName().concat(Optional.ofNullable(previousException.getMessage()).orElse(StringUtils.EMPTY)))
      .push();
}
