package com.demo.poc.commons.core.errors.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorDictionary {

  //system=00
  INVALID_FIELD("Invalid field", "02.00.01"),
  NO_SUCH_STRATEGY("No such strategy", "02.00.02"),
  NO_SUCH_LOGGER_TYPE("No such logger type", "02.00.03"),
  NO_SUCH_REST_CLIENT("No such rest client", "02.00.04"),
  ERROR_READING_JSON("Error reading JSON", "02.00.05"),
  ERROR_MAPPING_REFLECTIVE_PARAMS("Error mapping reflective params", "02.00.06"),
  ERROR_ASSIGN_REFLECTIVE_PARAMS("Error assign reflective params", "02.00.07"),
  INVALID_STREAMING_DATA("Streaming data is not processable", "02.00.08"),
  UNEXPECTED_SSL_EXCEPTION("Unexpected SSL error for HTTP client", "02.00.09"),

  //custom=01
  ROLE_NOT_FOUND("Expected role not found", "02.01.01"),
  MISSING_AUTHORIZATION_HEADER("Missing Authorization header", "02.01.02"),
  INVALID_AUTHORIZATION_STRUCTURE("Invalid Authorization structure", "02.01.03");

  private final String code;
  private final String message;
}
