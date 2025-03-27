package com.demo.poc.commons.core.errors.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorDictionary {

  //system=00
  INVALID_FIELD("Invalid field", "11.00.01"),
  NO_SUCH_STRATEGY("No such strategy", "11.00.02"),
  NO_SUCH_LOGGER_TYPE("No such logger type", "11.00.03"),
  NO_SUCH_REST_CLIENT("No such rest client", "11.00.04"),
  ERROR_READING_JSON("Error reading JSON", "11.00.05"),
  ERROR_MAPPING_REFLECTIVE_PARAMS("Error mapping reflective params", "11.00.06"),
  ERROR_ASSIGN_REFLECTIVE_PARAMS("Error assign reflective params", "11.00.07"),
  EMPTY_BASE_URL("Base URL is required", "11.00.08"),

  //custom=01
  ERROR_COMPILING_RULES("Error compiling rules", "11.01.01"),
  NO_SUCH_RULE_MAPPER("No such rule mapper", "11.01.02");

  private final String code;
  private final String message;
}
