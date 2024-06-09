package com.demo.bbq.config.tracing.logging.injector;

import static com.demo.bbq.config.tracing.logging.constants.ThreadContextConstant.*;

import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

public class ThreadContextInjectorUtil {

  private static void putInContext(String key, String value) {
    MDC.put(key, StringUtils.defaultString(value));
  }

  public static void populateFromHeaders(Map<String, String> traceHeaders) {
    traceHeaders.forEach(ThreadContextInjectorUtil::putInContext);
  }

  public static void populateFromRestClientRequest(String method, String uri,
                                                   String headers, String body) {
    putInContext(REST_CLIENT_REQ_METHOD, method);
    putInContext(REST_CLIENT_REQ_URI, uri);
    putInContext(REST_CLIENT_REQ_HEADERS, headers);
    putInContext(REST_CLIENT_REQ_BODY, body);
  }

  public static void populateFromRestClientResponse(String method, String uri,
                                                    String headers, String body,
                                                    String httpCode) {
    putInContext(REST_CLIENT_REQ_METHOD, method);
    putInContext(REST_CLIENT_REQ_URI, uri);
    putInContext(REST_CLIENT_RES_HEADERS, headers);
    putInContext(REST_CLIENT_RES_BODY, body);
    putInContext(REST_CLIENT_RES_STATUS, httpCode);
  }

  public static void populateFromRestServerRequest(String method, String uri,
                                                   String headers, String body) {
    putInContext(REST_SERVER_REQ_METHOD, method);
    putInContext(REST_SERVER_REQ_URI, uri);
    putInContext(REST_SERVER_REQ_HEADERS, headers);
    putInContext(REST_SERVER_REQ_BODY, body);
  }

  public static void populateFromRestServerResponse(String method, String uri,
                                                    String headers, String body,
                                                    String httpCode) {
    putInContext(REST_SERVER_REQ_METHOD, method);
    putInContext(REST_SERVER_REQ_URI, uri);
    putInContext(REST_SERVER_RES_HEADERS, headers);
    putInContext(REST_SERVER_RES_BODY, body);
    putInContext(REST_SERVER_RES_STATUS, httpCode);
  }
}
