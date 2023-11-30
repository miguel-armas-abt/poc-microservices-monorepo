package com.demo.bbq.business.menuoption.infrastructure.exception.catalog;

import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Getter
public enum ApiExceptionType {

  BUSINESS_RULES("01", "/errors/business-rules", HttpResponseStatus.BAD_REQUEST),

  AUTH_RULES("02", "/errors/auth-rules", HttpResponseStatus.UNAUTHORIZED),

  NO_DATA("03", "/errors/no-data", HttpResponseStatus.NOT_FOUND),
  GATEWAY_CONNECTION("04", "/errors/gateway-connection", HttpResponseStatus.BAD_GATEWAY),
  MALFORMED_REQUEST("05", "/errors/malformed-request", HttpResponseStatus.BAD_GATEWAY),
  UNEXPECTED("06", "/errors/unexpected", HttpResponseStatus.INTERNAL_SERVER_ERROR),
  TIMEOUT("07", "/errors/timeout", HttpResponseStatus.SERVICE_UNAVAILABLE);

  private final String code;
  private final String description;
  private final HttpResponseStatus httpStatus;
}
