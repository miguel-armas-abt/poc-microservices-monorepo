package com.demo.bbq.commons.core.errors.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorDictionary {

  //system=00
  INVALID_FIELD("Invalid field", "05.00.01"),
  NO_SUCH_STRATEGY("No such strategy", "05.00.02"),
  NO_SUCH_LOGGER_TYPE("No such logger type", "05.00.03"),
  NO_SUCH_REST_CLIENT("No such rest client", "05.00.04"),
  ERROR_READING_JSON("Error reading JSON", "05.00.05"),
  ERROR_MAPPING_REFLECTIVE_PARAMS("Error mapping reflective params", "05.00.06"),
  ERROR_ASSIGN_REFLECTIVE_PARAMS("Error assign reflective params", "05.00.07"),

  //custom=01
  MENU_OPTION_NOT_FOUND("The menu option does not exist", "05.01.01"),
  INVALID_MENU_CATEGORY("The menu category is not defined", "05.01.02"),;

  private final String code;
  private final String message;
}
