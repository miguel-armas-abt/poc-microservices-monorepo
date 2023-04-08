package com.demo.bbq.business.diningroomorder.domain.exception;

import com.demo.bbq.support.exception.model.ApiException;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpStatus;

@Slf4j
@AllArgsConstructor
@Getter
public enum DiningRoomOrderException {

  ERROR1000("/errors/no-data", "The dining room table does not exist", HttpStatus.BAD_REQUEST),
  ERROR1001("/errors/business-rules", "The requested menu order doesn't exist", HttpStatus.BAD_REQUEST);

  private final String type;
  private final String description;
  private final HttpStatus status;

  public ApiException buildExceptionFromThrowable(Throwable cause) {
    return ApiException.builder(this.type, this.name(), this.description, this.status)
        .cause(cause)
        .build();
  }

  public ApiException buildCustomException() {
    return ApiException.builder(this.type, this.name(), this.description, this.status)
        .build();
  }

  public ApiException buildCustomException(String additionalDescription) {
    return ApiException.builder(this.type, this.name(),
            this.description + " - " + Optional.ofNullable(additionalDescription).orElse(Strings.EMPTY), this.status)
        .build();
  }
}

