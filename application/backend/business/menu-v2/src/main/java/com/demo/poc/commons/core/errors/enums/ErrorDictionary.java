package com.demo.poc.commons.core.errors.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorDictionary {

  //system=00
  INVALID_FIELD("Invalid field", "06.00.01"),
  NO_SUCH_OBFUSCATION_TEMPLATE("No such obfuscation template", "06.00.04"),
  NO_SUCH_LOGGER_TYPE("No such logger type", "06.00.03"),

  //custom=01
  ;
  private final String code;
  private final String message;
}
