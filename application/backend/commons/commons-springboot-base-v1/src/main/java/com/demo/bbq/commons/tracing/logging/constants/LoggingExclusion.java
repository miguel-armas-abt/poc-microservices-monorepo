package com.demo.bbq.commons.tracing.logging.constants;

import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

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