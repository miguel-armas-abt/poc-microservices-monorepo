package com.demo.poc.commons.core.logging.enums;

import java.util.Objects;
import java.util.Optional;

import com.demo.poc.commons.core.properties.logging.LoggingTemplate;
import com.demo.poc.commons.custom.properties.ApplicationProperties;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

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

  public static boolean isLoggerPresent(ApplicationProperties properties, LoggingType loggingType) {
    return properties.logging()
        .filter(logging -> Objects.nonNull(logging.loggingType()))
        .map(LoggingTemplate::loggingType)
        .filter(loggers -> loggers.containsKey(loggingType.getCode()))
        .map(loggers -> loggers.get(loggingType.getCode()))
        .orElse(Boolean.TRUE);
  }
}