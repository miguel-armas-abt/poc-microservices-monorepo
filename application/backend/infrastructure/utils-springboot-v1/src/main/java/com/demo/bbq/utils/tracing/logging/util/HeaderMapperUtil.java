package com.demo.bbq.utils.tracing.logging.util;

import static com.demo.bbq.utils.tracing.logging.constants.ThreadContextConstant.*;

import java.util.HashMap;
import java.util.Map;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.WebRequest;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HeaderMapperUtil {

  public static Map<String, String> recoverTraceHeaders(HttpHeaders headers) {
    Map<String, String> mapHeaders = new HashMap<>();
    mapHeaders.put(TRACE_ID, headers.getFirst(TRACE_ID));
    mapHeaders.put(PARENT_ID, headers.getFirst(PARENT_ID));
    mapHeaders.put(TRACE_PARENT, headers.getFirst(TRACE_PARENT));
    return mapHeaders;
  }

  public static Map<String, String> recoverTraceHeaders(WebRequest request) {
    Map<String, String> mapHeaders = new HashMap<>();
    mapHeaders.put(TRACE_ID, request.getHeader(TRACE_ID));
    mapHeaders.put(PARENT_ID, request.getHeader(PARENT_ID));
    mapHeaders.put(TRACE_PARENT, request.getHeader(TRACE_PARENT));
    return mapHeaders;
  }
}
