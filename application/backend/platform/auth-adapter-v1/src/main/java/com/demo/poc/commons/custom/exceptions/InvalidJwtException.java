package com.demo.poc.commons.custom.exceptions;

import com.demo.poc.commons.core.errors.exceptions.GenericException;
import lombok.Getter;

@Getter
public class InvalidJwtException extends GenericException {

  public InvalidJwtException(String message) {
    super(message, ErrorDictionary.parse(InvalidJwtException.class));
  }
}
