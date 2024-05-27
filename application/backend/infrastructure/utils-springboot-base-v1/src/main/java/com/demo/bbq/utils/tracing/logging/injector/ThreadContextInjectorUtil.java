package com.demo.bbq.utils.tracing.logging.injector;

import static com.demo.bbq.utils.tracing.logging.constants.ThreadContextConstant.*;

import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.ThreadContext;

public class ThreadContextInjectorUtil {

  public static void populateFromHeaders(Map<String, String> traceHeaders) {
    ThreadContext.put(TRACE_ID, getNullableHeader(traceHeaders.get(TRACE_ID)));
    ThreadContext.put(PARENT_ID, getNullableHeader(traceHeaders.get(PARENT_ID)));
    ThreadContext.put(TRACE_PARENT, getNullableHeader(traceHeaders.get(TRACE_PARENT)));
  }

  private static String getNullableHeader(String header) {
    return header != null ? header : StringUtils.EMPTY;
  }

  public static void populateFromClientRequest(String method, String uri,
                                               String headers, String body) {
    ThreadContext.put(REQ_METHOD, method);
    ThreadContext.put(REQ_URI, uri);
    ThreadContext.put(REQ_HEADERS, headers);
    ThreadContext.put(REQ_BODY, body);
  }

  public static void populateFromClientResponse(String method, String uri,
                                                String headers, String body,
                                                String httpCode) {
    ThreadContext.put(REQ_METHOD, method);
    ThreadContext.put(REQ_URI, uri);
    ThreadContext.put(RES_HEADERS, headers);
    ThreadContext.put(RES_BODY, body);
    ThreadContext.put(RES_STATUS, httpCode);
  }
}
