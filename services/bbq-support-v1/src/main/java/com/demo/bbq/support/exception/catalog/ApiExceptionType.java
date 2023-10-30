package com.demo.bbq.support.exception.catalog;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
@AllArgsConstructor
@Getter
public enum ApiExceptionType {

  BUSINESS_RULES("01", "/errors/business-rules", HttpStatus.BAD_REQUEST),

  AUTH_RULES("02", "/errors/auth-rules", HttpStatus.UNAUTHORIZED),

  NO_DATA("03", "/errors/no-data", HttpStatus.NOT_FOUND),
  GATEWAY_CONNECTION("04", "/errors/gateway-connection", HttpStatus.BAD_GATEWAY),
  MALFORMED_REQUEST("05", "/errors/malformed-request", HttpStatus.BAD_GATEWAY);

  private final String code;
  private final String description;
  private final HttpStatus httpStatus;
}
