package com.demo.poc.commons.custom.exceptions;

import com.demo.poc.commons.core.errors.exceptions.GenericException;
import lombok.Getter;

@Getter
public class InvalidMenuCategoryException extends GenericException {

  public InvalidMenuCategoryException() {
    super(ErrorDictionary.MENU_OPTION_NOT_FOUND.getMessage(), ErrorDictionary.parse(InvalidMenuCategoryException.class));
  }
}
