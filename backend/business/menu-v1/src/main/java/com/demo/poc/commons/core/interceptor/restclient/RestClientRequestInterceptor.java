package com.demo.poc.commons.core.interceptor.restclient;

import com.demo.poc.commons.core.logging.ThreadContextInjector;
import com.demo.poc.commons.core.logging.dto.RestRequestLog;
import com.demo.poc.commons.core.logging.enums.LoggingType;
import com.demo.poc.commons.core.restclient.utils.HeadersExtractor;
import com.demo.poc.commons.core.tracing.enums.TraceParam;
import com.demo.poc.commons.custom.properties.ApplicationProperties;
import jakarta.ws.rs.client.ClientRequestContext;
import jakarta.ws.rs.client.ClientRequestFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class RestClientRequestInterceptor implements ClientRequestFilter {

  private final ApplicationProperties properties;
  private final ThreadContextInjector contextInjector;

  @Override
  public void filter(ClientRequestContext requestContext) {
    generateTrace(requestContext);
  }

  private void generateTrace(ClientRequestContext requestContext) {
    boolean isLoggerPresent = LoggingType.isLoggerPresent(properties, LoggingType.REST_CLIENT_REQ);
    if (isLoggerPresent) {
      Map<String, String> headers = HeadersExtractor.extractHeadersAsMap(requestContext.getHeaders());

      RestRequestLog log = RestRequestLog.builder()
          .method(requestContext.getMethod())
          .uri(requestContext.getUri().toString())
          .requestHeaders(headers)
          .requestBody("{\"to\":\"do\"}")
          .traceParent(headers.get(TraceParam.TRACE_PARENT.getKey()))
          .build();
      contextInjector.populateFromRestRequest(LoggingType.REST_CLIENT_REQ, log);
    }
  }


}