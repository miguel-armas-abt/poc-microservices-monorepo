package com.demo.poc.commons.core.errors.exceptions;

import com.demo.poc.commons.custom.exceptions.ErrorDictionary;
import lombok.Getter;

@Getter
public class UnexpectedSslException extends GenericException {

  public UnexpectedSslException(String message) {
    super(message, ErrorDictionary.parse(UnexpectedSslException.class));
  }
}