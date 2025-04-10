package com.demo.poc.commons.custom.exceptions;

import com.demo.poc.commons.core.errors.exceptions.GenericException;
import lombok.Getter;

@Getter
public class MenuOptionNotFoundException extends GenericException {

  public MenuOptionNotFoundException() {
    super(ErrorDictionary.MENU_OPTION_NOT_FOUND.getMessage(), ErrorDictionary.parse(MenuOptionNotFoundException.class));
  }
}
