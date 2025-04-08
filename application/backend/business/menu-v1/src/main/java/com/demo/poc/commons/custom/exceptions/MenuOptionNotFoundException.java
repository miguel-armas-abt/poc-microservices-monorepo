package com.demo.poc.commons.custom.exceptions;

import com.demo.poc.commons.core.errors.dto.ErrorDTO;
import com.demo.poc.commons.core.errors.enums.ErrorDictionary;
import com.demo.poc.commons.core.errors.exceptions.GenericException;
import lombok.Getter;

@Getter
public class MenuOptionNotFoundException extends GenericException {

  public MenuOptionNotFoundException() {
    super(ErrorDictionary.MENU_OPTION_NOT_FOUND.getMessage());

    ErrorDictionary detail = ErrorDictionary.parse(this.getClass());
    this.httpStatus = detail.getHttpStatus();
    this.errorDetail = ErrorDTO.builder()
            .type(detail.getType())
            .code(detail.getCode())
            .message(detail.getMessage())
            .build();
  }
}
