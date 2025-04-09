package com.demo.poc.commons.core.logging.enums;

import com.demo.poc.commons.core.errors.exceptions.NoSuchLoggerTypeException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

@Getter
@RequiredArgsConstructor
public enum LoggingType {

  ERROR("error", "Error"),
  REST_SERVER_REQ("rest.server.req", "REST server request"),
  REST_SERVER_RES("rest.server.res", "REST server response"),
  REST_CLIENT_REQ("rest.client.req", "REST client request"),
  REST_CLIENT_RES("rest.client.res", "REST client response");

  private final String code;
  private final String message;

  public LoggingType findByCode(String code) {
    return Arrays.stream(values())
        .filter(loggerType -> loggerType.getCode().equals(code))
        .findFirst()
        .orElseThrow(NoSuchLoggerTypeException::new);
  }

  public static boolean isLoggerPresent(LoggingType loggingType, Map<String, Boolean> loggers) {
    return Optional.ofNullable(loggers)
            .filter(enabledLoggers -> enabledLoggers.containsKey(loggingType.getCode()))
            .map(enabledLoggers -> enabledLoggers.get(loggingType.getCode()))
            .orElseGet(() -> Boolean.FALSE);
  }
}