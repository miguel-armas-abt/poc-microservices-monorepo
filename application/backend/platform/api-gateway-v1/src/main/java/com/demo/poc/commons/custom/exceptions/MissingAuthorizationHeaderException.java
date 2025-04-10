package com.demo.poc.commons.custom.exceptions;

import com.demo.poc.commons.core.errors.exceptions.GenericException;
import lombok.Getter;

@Getter
public class MissingAuthorizationHeaderException extends GenericException {

  public MissingAuthorizationHeaderException() {
    super(ErrorDictionary.MISSING_AUTHORIZATION_HEADER.getMessage(), ErrorDictionary.parse(MissingAuthorizationHeaderException.class));
  }
}
