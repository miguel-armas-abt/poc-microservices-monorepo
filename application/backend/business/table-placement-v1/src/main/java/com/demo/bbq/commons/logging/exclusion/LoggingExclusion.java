package com.demo.bbq.commons.logging.exclusion;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoggingExclusion {

  public static final List<String> EXCLUSIONS_ENDPOINT = List.of(
      "/actuator/prometheus"
  );

  public static boolean isExcluded(String endpoint) {
    return EXCLUSIONS_ENDPOINT
        .stream()
        .map(endpoint::contains)
        .filter(Boolean.TRUE::equals)
        .findFirst()
        .orElseGet(() -> Boolean.FALSE);
  }

}