package com.demo.bbq.commons.custom.exceptions;

import com.demo.bbq.commons.core.errors.dto.ErrorDTO;
import com.demo.bbq.commons.core.errors.enums.ErrorDictionary;
import com.demo.bbq.commons.core.errors.exceptions.GenericException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UnableLogoutException extends GenericException {

  private static final ErrorDictionary EXCEPTION = ErrorDictionary.UNABLE_LOGOUT;

  public UnableLogoutException(String message) {
    super(message);
    this.httpStatus = HttpStatus.UNAUTHORIZED;
    this.errorDetail = ErrorDTO.builder()
        .code(EXCEPTION.getCode())
        .message(EXCEPTION.getMessage())
        .build();
  }
}
