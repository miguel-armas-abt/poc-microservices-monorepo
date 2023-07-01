package com.demo.bbq.support.exception.catalog;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
@AllArgsConstructor
@Getter
public enum ApiExceptionType {

  NO_DATA("01", "/errors/no-data", HttpStatus.BAD_REQUEST),
  BUSINESS_RULES("02", "/errors/business-rules", HttpStatus.BAD_REQUEST),
  AUTH_RULES("03", "/errors/auth-rules", HttpStatus.UNAUTHORIZED),
  GATEWAY_CONNECTION("04", "/errors/gateway-connection", HttpStatus.BAD_GATEWAY),
  MALFORMED_REQUEST("05", "/errors/malformed-request", HttpStatus.BAD_GATEWAY);

  private final String code;
  private final String description;
  private final HttpStatus httpStatus;
}
