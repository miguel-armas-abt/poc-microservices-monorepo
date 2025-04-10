package com.demo.poc.commons.core.errors.exceptions;

import com.demo.poc.commons.custom.exceptions.ErrorDictionary;
import lombok.Getter;

@Getter
public class NoSuchLoggerTypeException extends GenericException {

  public NoSuchLoggerTypeException() {
    super(ErrorDictionary.NO_SUCH_LOGGER_TYPE.getMessage(), ErrorDictionary.parse(NoSuchLoggerTypeException.class));

  }
}