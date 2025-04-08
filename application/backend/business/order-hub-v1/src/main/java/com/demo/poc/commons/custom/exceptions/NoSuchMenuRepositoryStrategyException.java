package com.demo.poc.commons.custom.exceptions;

import com.demo.poc.commons.core.errors.dto.ErrorDTO;
import com.demo.poc.commons.core.errors.enums.ErrorDictionary;
import com.demo.poc.commons.core.errors.exceptions.GenericException;
import lombok.Getter;

@Getter
public class NoSuchMenuRepositoryStrategyException extends GenericException {

  public NoSuchMenuRepositoryStrategyException() {
    super(ErrorDictionary.NO_SUCH_MENU_REPOSITORY_STRATEGY.getMessage());

    ErrorDictionary detail = ErrorDictionary.parse(this.getClass());
    this.httpStatus = detail.getHttpStatus();
    this.errorDetail = ErrorDTO.builder()
            .type(detail.getType())
            .code(detail.getCode())
            .message(detail.getMessage())
            .build();
  }
}
