package com.demo.poc.commons.custom.exceptions;

import com.demo.poc.commons.core.errors.dto.ErrorDTO;
import com.demo.poc.commons.core.errors.enums.ErrorDictionary;
import com.demo.poc.commons.core.errors.exceptions.GenericException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UnableRefreshException extends GenericException {

  private static final ErrorDictionary EXCEPTION = ErrorDictionary.UNABLE_REFRESH;

  public UnableRefreshException() {
    super(EXCEPTION.getMessage());
    this.httpStatus = HttpStatus.UNAUTHORIZED;
    this.errorDetail = ErrorDTO.builder()
        .code(EXCEPTION.getCode())
        .message(EXCEPTION.getMessage())
        .build();
  }
}
