package com.demo.poc.commons.custom.exceptions;

import com.demo.poc.commons.core.errors.enums.ErrorDictionary;
import com.demo.poc.commons.core.errors.exceptions.GenericException;
import lombok.Getter;

@Getter
public class UnableLogoutException extends GenericException {

  public UnableLogoutException(String message) {
    super(message, ErrorDictionary.parse(UnableLogoutException.class));
  }
}
