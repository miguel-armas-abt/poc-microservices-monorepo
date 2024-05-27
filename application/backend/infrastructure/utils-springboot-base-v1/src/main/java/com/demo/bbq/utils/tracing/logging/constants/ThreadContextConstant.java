package com.demo.bbq.utils.tracing.logging.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Contains constants for thread context keys used in MDC.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ThreadContextConstant {

  public static final String TRACE_ID = "traceId";
  public static final String PARENT_ID = "parentId";
  public static final String TRACE_PARENT = "traceParent";

  public static final String REQ_METHOD = "req.method";
  public static final String REQ_URI = "req.uri";
  public static final String REQ_HEADERS = "req.headers";
  public static final String REQ_BODY = "req.body";
  public static final String RES_STATUS = "res.status";
  public static final String RES_HEADERS = "res.headers";
  public static final String RES_BODY = "res.body";

  public static final String TRACKING_INFO = "tracking.info";

}
