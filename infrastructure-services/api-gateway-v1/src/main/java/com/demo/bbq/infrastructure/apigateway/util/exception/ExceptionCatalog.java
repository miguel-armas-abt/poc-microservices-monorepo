package com.demo.bbq.infrastructure.apigateway.util.exception;

import com.demo.bbq.support.exception.model.ApiException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
@AllArgsConstructor
@Getter
public enum ExceptionCatalog {

  ERROR1000("/errors/auth-rules", "Missing  Authorization header", HttpStatus.UNAUTHORIZED),
  ERROR1001("/errors/auth-rules", "Bad Authorization structure", HttpStatus.UNAUTHORIZED),
  ERROR1002("/errors/auth-rules", "Roles missing", HttpStatus.UNAUTHORIZED),
  ERROR1003("/errors/http-connection", "Connection error", HttpStatus.UNAUTHORIZED);

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
