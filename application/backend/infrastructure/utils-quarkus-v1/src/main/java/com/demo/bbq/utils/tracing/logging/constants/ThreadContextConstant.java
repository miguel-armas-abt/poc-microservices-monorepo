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

  //rest server keys
  public static final String REST_SERVER_REQ_METHOD = "rest.server.req.method";
  public static final String REST_SERVER_REQ_URI = "rest.server.req.uri";
  public static final String REST_SERVER_REQ_HEADERS = "rest.server.req.headers";
  public static final String REST_SERVER_REQ_BODY = "rest.server.req.body";
  public static final String REST_SERVER_RES_STATUS = "rest.server.res.status";
  public static final String REST_SERVER_RES_HEADERS = "rest.server.res.headers";
  public static final String REST_SERVER_RES_BODY = "rest.server.res.body";

  //rest client keys
  public static final String REST_CLIENT_REQ_METHOD = "rest.client.req.method";
  public static final String REST_CLIENT_REQ_URI = "rest.client.req.uri";
  public static final String REST_CLIENT_REQ_HEADERS = "rest.client.req.headers";
  public static final String REST_CLIENT_REQ_BODY = "rest.client.req.body";
  public static final String REST_CLIENT_RES_STATUS = "rest.client.res.status";
  public static final String REST_CLIENT_RES_HEADERS = "rest.client.res.headers";
  public static final String REST_CLIENT_RES_BODY = "rest.client.res.body";

  public static final String TRACKING_INFO = "tracking.info";

}
