package com.demo.bbq.commons.custom.exceptions;

import com.demo.bbq.commons.core.errors.dto.ErrorDTO;
import com.demo.bbq.commons.core.errors.enums.ErrorDictionary;
import com.demo.bbq.commons.core.errors.exceptions.GenericException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class InvalidJwtException extends GenericException {

  private static final ErrorDictionary EXCEPTION = ErrorDictionary.INVALID_JWT;

  public InvalidJwtException(String message) {
    super(message);
    this.httpStatus = HttpStatus.UNAUTHORIZED;
    this.errorDetail = ErrorDTO.builder()
        .code(EXCEPTION.getCode())
        .message(EXCEPTION.getMessage())
        .build();
  }
}
