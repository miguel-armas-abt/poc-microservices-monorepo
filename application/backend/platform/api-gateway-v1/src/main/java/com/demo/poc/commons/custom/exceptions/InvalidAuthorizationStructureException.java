package com.demo.poc.commons.custom.exceptions;

import com.demo.poc.commons.core.errors.enums.ErrorDictionary;
import com.demo.poc.commons.core.errors.exceptions.GenericException;
import lombok.Getter;

@Getter
public class InvalidAuthorizationStructureException extends GenericException {

  public InvalidAuthorizationStructureException() {
    super(ErrorDictionary.INVALID_AUTHORIZATION_STRUCTURE.getMessage(), ErrorDictionary.parse(InvalidAuthorizationStructureException.class));
  }
}
