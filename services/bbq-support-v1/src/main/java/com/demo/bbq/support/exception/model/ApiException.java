package com.demo.bbq.support.exception.model;

import com.demo.bbq.support.exception.model.builder.ApiExceptionBuilder;
import java.util.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ApiException extends RuntimeException {
  private String message;
  private Throwable cause;
  private String type;
  private String errorCode;
  private HttpStatus httpStatus;
  private List<ApiExceptionDetail> details;

  public ApiException(String message, Throwable cause, String type, String errorCode,
                      HttpStatus httpStatus, List<ApiExceptionDetail> details) {
    super(message, cause);
    this.type = type;
    this.message = message;
    this.errorCode = errorCode;
    this.details = Optional.ofNullable(details)
        .map(Collections::unmodifiableList)
        .orElseGet(Collections::emptyList);
    this.httpStatus = httpStatus;
  }

  public static ApiExceptionBuilder builder() {
    return new ApiExceptionBuilder();
  }

  public List<ApiExceptionDetail> getDetails() {
    if (this.getCause() instanceof ApiException) {
      List<ApiExceptionDetail> details = ((ApiException) this.getCause()).getDetails();
      List<ApiExceptionDetail> newDetails = new ArrayList<>();
      newDetails.addAll(this.details);
      newDetails.addAll(details);
      return Collections.unmodifiableList(newDetails);
    } else {
      return this.details;
    }
  }
}
