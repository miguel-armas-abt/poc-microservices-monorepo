package com.demo.poc.commons.core.errors.exceptions;

import com.demo.poc.commons.core.errors.enums.ErrorDictionary;
import lombok.Getter;

@Getter
public class NoSuchRestClientErrorStrategyException extends GenericException {

  public NoSuchRestClientErrorStrategyException() {
    super(ErrorDictionary.NO_SUCH_REST_CLIENT_ERROR_STRATEGY.getMessage(), ErrorDictionary.parse(NoSuchRestClientErrorStrategyException.class));
  }
}
