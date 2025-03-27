package com.demo.poc.commons.core.errors.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorDictionary {

  //system=00
  INVALID_FIELD("Invalid field", "03.00.01"),
  NO_SUCH_STRATEGY("No such strategy", "03.00.02"),
  NO_SUCH_LOGGER_TYPE("No such logger type", "03.00.03"),
  NO_SUCH_REST_CLIENT("No such rest client", "03.00.04"),
  ERROR_READING_JSON("Error reading JSON", "03.00.05"),
  ERROR_MAPPING_REFLECTIVE_PARAMS("Error mapping reflective params", "03.00.06"),
  ERROR_ASSIGN_REFLECTIVE_PARAMS("Error assign reflective params", "03.00.07"),
  EMPTY_BASE_URL("Base URL is required", "03.00.08"),

  //custom=01
  UNABLE_LOGOUT("Unable logout", "03.01.01"),
  UNABLE_REFRESH("Unable refresh", "03.01.02"),
  INVALID_JWT("Invalid JWT", "03.01.03"),
  EXPIRED_TOKEN("Expired token", "03.01.04"),;

  private final String code;
  private final String message;
}
