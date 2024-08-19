package com.demo.bbq.commons.tracing.logging.util;

import com.demo.bbq.commons.toolkit.params.enums.GeneratedParamType;
import com.demo.bbq.commons.tracing.logging.constants.ThreadContextConstant;

import java.util.HashMap;
import java.util.Map;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HeaderMapperUtil {

  public static Map<String, String> recoverTraceHeaders(HttpHeaders headers) {
    Map<String, String> mapHeaders = new HashMap<>();
    String traceId = headers.getFirst(GeneratedParamType.TRACE_ID.getKey());
    String parentId = headers.getFirst(GeneratedParamType.PARENT_ID.getKey());

    mapHeaders.put(ThreadContextConstant.TRACE_ID, traceId);
    mapHeaders.put(ThreadContextConstant.PARENT_ID, parentId);
    mapHeaders.put(ThreadContextConstant.TRACE_PARENT, traceId + "-" + parentId);
    return mapHeaders;
  }
}
