package com.demo.poc.commons.core.errors.exceptions;

import com.demo.poc.commons.core.errors.dto.ErrorDTO;
import com.demo.poc.commons.core.errors.enums.ErrorDictionary;
import jakarta.ws.rs.core.Response;
import lombok.Getter;

@Getter
public class NoSuchLoggerTypeException extends GenericException {

  private static final ErrorDictionary EXCEPTION = ErrorDictionary.NO_SUCH_LOGGER_TYPE;

  public NoSuchLoggerTypeException() {
    super(EXCEPTION.getMessage());
    this.httpStatus = Response.Status.BAD_REQUEST;
    this.errorDetail = ErrorDTO.builder()
        .code(EXCEPTION.getCode())
        .message(EXCEPTION.getMessage())
        .build();
  }
}