package com.demo.bbq.commons.core.errors.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorDictionary {

  //system=00
  INVALID_FIELD("Invalid field", "08.00.01"),
  NO_SUCH_STRATEGY("No such strategy", "08.00.02"),
  NO_SUCH_LOGGER_TYPE("No such logger type", "08.00.03"),
  NO_SUCH_REST_CLIENT("No such rest client", "08.00.04"),
  ERROR_READING_JSON("Error reading JSON", "08.00.05"),
  ERROR_MAPPING_REFLECTIVE_PARAMS("Error mapping reflective params", "08.00.06"),
  ERROR_ASSIGN_REFLECTIVE_PARAMS("Error assign reflective params", "08.00.07"),
  INVALID_STREAMING_DATA("Streaming data is not processable", "08.00.08"),
  UNEXPECTED_SSL_EXCEPTION("Unexpected SSL error for HTTP client", "08.00.09"),

  //custom=01
  PAYMENT_STATUS_NON_UPDATED_EXCEPTION("Payment status could not be updated", "08.01.01"),
  TOTAL_AMOUNT_LESS_THAN_ZERO("Total amount less than zero", "08.01.02"),;

  private final String code;
  private final String message;
}
