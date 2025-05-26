package com.demo.poc.commons.core.logging;

import java.util.Map;

import com.demo.poc.commons.core.tracing.enums.TraceParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.ThreadContext;

import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.server.ServerWebExchange;

import static com.demo.poc.commons.core.logging.ThreadContextInjector.RestConstants.METHOD;
import static com.demo.poc.commons.core.logging.ThreadContextInjector.RestConstants.URI;
import static com.demo.poc.commons.core.logging.enums.LoggingType.REST_CLIENT_REQ;

@Slf4j
@RequiredArgsConstructor
public class ErrorThreadContextInjector {

  private final ThreadContextInjector injector;

  public void populateFromException(Throwable ex, ServerWebExchange exchange) {
    ThreadContext.clearAll();
    if (ex instanceof WebClientRequestException webClientRequestException) {
      ThreadContext.put(REST_CLIENT_REQ.getCode() + METHOD, webClientRequestException.getMethod().toString());
      ThreadContext.put(REST_CLIENT_REQ.getCode() + URI, webClientRequestException.getUri().toString());
    }

    String traceParent = exchange.getRequest().getHeaders().getFirst(TraceParam.TRACE_PARENT.getKey());
    Map<String, String> traceHeaders = TraceParam.Util.getTraceHeadersAsMap(traceParent);
    injector.populateFromHeaders(traceHeaders);
    log.error(ex.getMessage(), ex);
  }
}
