package com.demo.poc.commons.custom.exceptions;

import com.demo.poc.commons.core.errors.exceptions.GenericException;
import lombok.Getter;

@Getter
public class ExpiredTokenException extends GenericException {

  public ExpiredTokenException() {
    super(ErrorDictionary.EXPIRED_TOKEN.getMessage(), ErrorDictionary.parse(ExpiredTokenException.class));
  }
}
