package com.demo.bbq.utils.interceptor.logging.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoggingConstant {

  public static final String HEADER_TRACE = "traceId";
  public static final String HEADER_PARENT = "parentId";
  public static final String HEADER_TRACEPARENT = "traceparent";
  public static final String MDC_NAME_COMPONENT = "tracking.nameComponent";
  public static final String MDC_TRACE_ID = "tracking.traceId";
  public static final String MDC_PARENT_ID = "tracking.parentId";
  public static final String MDC_TRACEPARENT = "tracking.traceParent";

  public static final String MDC_REQ_METHOD = "req.method";
  public static final String MDC_REQ_URI = "req.uri";
  public static final String MDC_REQ_HEADERS = "req.headers";
  public static final String MDC_REQ_BODY = "req.body";
  public static final String MDC_RES_STATUS = "res.status";
  public static final String MDC_RES_HEADERS = "res.headers";
  public static final String MDC_RES_BODY = "res.body";

  public static final String TRACKING_INFO = "tracking.info";

}
