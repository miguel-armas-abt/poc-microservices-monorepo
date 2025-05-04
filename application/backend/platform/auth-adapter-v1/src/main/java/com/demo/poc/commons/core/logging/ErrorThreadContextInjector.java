package com.demo.poc.commons.core.logging;

import com.demo.poc.commons.core.tracing.enums.TraceParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.ThreadContext;

import org.springframework.web.context.request.WebRequest;

@Slf4j
@RequiredArgsConstructor
public class ErrorThreadContextInjector {

  private final ThreadContextInjector injector;

  public void populateFromException(Throwable ex,  WebRequest webRequest) {
    ThreadContext.clearAll();
    String traceParent = webRequest.getHeader(TraceParam.TRACE_PARENT.getKey());
    injector.populateFromHeaders(TraceParam.Util.getTraceHeadersAsMap(traceParent));
    log.error(ex.getMessage(), ex);
  }
}
