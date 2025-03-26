package com.demo.bbq.commons.core.errors.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorDictionary {

  //system
  INVALID_FIELD("Invalid field", "01.01.01"),
  NO_SUCH_STRATEGY("No such strategy", "01.01.02"),
  NO_SUCH_LOGGER_TYPE("No such logger type", "01.01.03"),
  NO_SUCH_REST_CLIENT("No such rest client", "01.01.04"),
  ERROR_READING_JSON("Error reading JSON", "01.01.05"),
  ERROR_MAPPING_REFLECTIVE_PARAMS("Error mapping reflective params", "01.01.06"),
  ERROR_ASSIGN_REFLECTIVE_PARAMS("Error assign reflective params", "01.01.07"),

  //custom
  MENU_OPTION_NOT_FOUND("The menu option does not exist", "01.02.01"),
  INVALID_MENU_CATEGORY("The menu category is not defined", "01.02.02"),;

  private final String code;
  private final String message;
}
