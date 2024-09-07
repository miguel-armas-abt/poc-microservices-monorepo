package com.demo.bbq.commons.tracing.logging.injector;

import static com.demo.bbq.commons.toolkit.params.enums.GeneratedParamType.TRACE_ID;
import static com.demo.bbq.commons.toolkit.params.enums.GeneratedParamType.PARENT_ID;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Contains constants for thread context keys used in MDC.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ThreadContextUtils {

  public static final String[] TRACE_HEADERS = {
      TRACE_ID.getKey(),
      PARENT_ID.getKey(),
      "trace-parent"
  };

  public static final String METHOD = ".method";
  public static final String URI = ".uri";
  public static final String HEADERS = ".headers";
  public static final String BODY = ".body";
  public static final String STATUS = ".status";

  public static Map<String, String> extractTraceFieldsFromHeaders(Map<String, String> headers) {
    return Arrays.stream(TRACE_HEADERS)
        .map(traceField -> Map.entry(traceField, Optional.ofNullable(headers.get(traceField))))
        .filter(entry -> entry.getValue().isPresent())
        .collect(Collectors.toMap(entry -> toCamelCase(entry.getKey()), entry -> entry.getValue().get()));
  }

  public static String toCamelCase(String value) {
    String[] parts = value.split("-");
    return parts[0] + Arrays.stream(parts, 1, parts.length)
        .map(part -> part.substring(0, 1).toUpperCase() + part.substring(1))
        .collect(Collectors.joining());
  }

}
