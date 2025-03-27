package com.demo.poc.commons.core.errors.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorDictionary {

  //system=00
  INVALID_FIELD("Invalid field", "07.00.01"),
  NO_SUCH_STRATEGY("No such strategy", "07.00.02"),
  NO_SUCH_LOGGER_TYPE("No such logger type", "07.00.03"),
  NO_SUCH_REST_CLIENT("No such rest client", "07.00.04"),
  ERROR_READING_JSON("Error reading JSON", "07.00.05"),
  ERROR_MAPPING_REFLECTIVE_PARAMS("Error mapping reflective params", "07.00.06"),
  ERROR_ASSIGN_REFLECTIVE_PARAMS("Error assign reflective params", "07.00.07"),
  INVALID_STREAMING_DATA("Streaming data is not processable", "07.00.08"),
  UNEXPECTED_SSL_EXCEPTION("Unexpected SSL error for HTTP client", "07.00.09"),

  //custom=01
  TABLE_NOT_FOUND("The table does not exist", "07.01.01");

  private final String code;
  private final String message;
}
