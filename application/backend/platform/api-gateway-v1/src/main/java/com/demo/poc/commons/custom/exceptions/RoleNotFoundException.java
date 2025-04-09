package com.demo.poc.commons.custom.exceptions;

import com.demo.poc.commons.core.errors.enums.ErrorDictionary;
import com.demo.poc.commons.core.errors.exceptions.GenericException;
import lombok.Getter;

@Getter
public class RoleNotFoundException extends GenericException {

  public RoleNotFoundException() {
    super(ErrorDictionary.ROLE_NOT_FOUND.getMessage(), ErrorDictionary.parse(RoleNotFoundException.class));
  }
}
