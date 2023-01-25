package com.demo.bbq.infrastructure.authadapter.util.exception;

import com.demo.bbq.support.exception.model.ApiException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
@AllArgsConstructor
@Getter
public enum ExceptionCatalog {

  ERROR1000("/errors/auth-rules", "Token validation error", HttpStatus.FORBIDDEN),
  ERROR1001("/errors/auth-rules", "Token validation error", HttpStatus.FORBIDDEN),
  ERROR1002("/errors/auth-rules", "Unable to logout", HttpStatus.FORBIDDEN),
  ERROR1003("/errors/auth-rules", "Unable to refresh", HttpStatus.FORBIDDEN);

  private final String type;
  private final String title;
  private final HttpStatus status;

  public ApiException buildException(Throwable cause) {
    return (cause instanceof ApiException)
        ? (ApiException) cause
        : ApiException.builder(this.type, this.name(), this.title, this.status)
        .build();
  }

  public ApiException buildException() {
    return ApiException.builder(this.type, this.name(), this.title, this.status).build();
  }

}
