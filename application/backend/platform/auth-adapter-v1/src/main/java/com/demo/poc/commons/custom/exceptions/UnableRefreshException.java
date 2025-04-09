package com.demo.poc.commons.custom.exceptions;

import com.demo.poc.commons.core.errors.enums.ErrorDictionary;
import com.demo.poc.commons.core.errors.exceptions.GenericException;
import lombok.Getter;

@Getter
public class UnableRefreshException extends GenericException {

  public UnableRefreshException() {
    super(ErrorDictionary.UNABLE_REFRESH.getMessage(), ErrorDictionary.parse(UnableRefreshException.class));
  }
}
