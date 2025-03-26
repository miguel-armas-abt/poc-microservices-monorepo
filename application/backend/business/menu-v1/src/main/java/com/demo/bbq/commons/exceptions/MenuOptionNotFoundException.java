package com.demo.bbq.commons.exceptions;

import com.demo.bbq.commons.core.errors.dto.ErrorDTO;
import com.demo.bbq.commons.core.errors.enums.ErrorDictionary;
import com.demo.bbq.commons.core.errors.exceptions.GenericException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class MenuOptionNotFoundException extends GenericException {

  private static final ErrorDictionary EXCEPTION = ErrorDictionary.MENU_OPTION_NOT_FOUND;

  public MenuOptionNotFoundException() {
    super(EXCEPTION.getMessage());
    this.httpStatus = HttpStatus.BAD_REQUEST;
    this.errorDetail = ErrorDTO.builder()
        .code(EXCEPTION.getCode())
        .message(EXCEPTION.getMessage())
        .build();
  }
}
