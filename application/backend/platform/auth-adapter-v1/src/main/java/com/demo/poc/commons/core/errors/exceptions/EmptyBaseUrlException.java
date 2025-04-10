package com.demo.poc.commons.core.errors.exceptions;

import com.demo.poc.commons.custom.exceptions.ErrorDictionary;
import lombok.Getter;

@Getter
public class EmptyBaseUrlException extends GenericException {

  public EmptyBaseUrlException() {
    super(ErrorDictionary.EMPTY_BASE_URL.getMessage(), ErrorDictionary.parse(EmptyBaseUrlException.class));
  }
}
