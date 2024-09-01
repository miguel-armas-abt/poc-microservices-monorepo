package com.demo.bbq.commons.tracing.logging.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Contains constants for thread context keys used in MDC.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ThreadContextConstant {

  public static final String[] TRACE_FIELDS = {"traceId", "parentId", "traceParent"};

  public static final String METHOD = ".method";
  public static final String URI = ".uri";
  public static final String HEADERS = ".headers";
  public static final String BODY = ".body";
  public static final String STATUS = ".status";

}
