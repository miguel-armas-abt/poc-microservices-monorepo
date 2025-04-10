package com.demo.poc.commons.custom.exceptions;

import com.demo.poc.commons.core.errors.exceptions.GenericException;
import lombok.Getter;

@Getter
public class TableNotFoundException extends GenericException {

  public TableNotFoundException() {
    super(ErrorDictionary.TABLE_NOT_FOUND.getMessage(), ErrorDictionary.parse(TableNotFoundException.class));
  }
}