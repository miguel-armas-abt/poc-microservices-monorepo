package com.demo.bbq.commons.core.errors.exceptions;

import com.demo.bbq.commons.core.errors.dto.ErrorDTO;
import com.demo.bbq.commons.core.errors.enums.ErrorDictionary;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class NoSuchStrategyException extends GenericException {

  private static final ErrorDictionary EXCEPTION = ErrorDictionary.NO_SUCH_STRATEGY;

  public NoSuchStrategyException() {
    super(EXCEPTION.getMessage());
    this.httpStatus = HttpStatus.BAD_REQUEST;
    this.errorDetail = ErrorDTO.builder()
        .code(EXCEPTION.getCode())
        .message(EXCEPTION.getMessage())
        .build();
  }
}
