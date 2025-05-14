package com.demo.poc.commons.core.interceptor.restclient;

import com.demo.poc.commons.core.logging.ThreadContextInjector;
import com.demo.poc.commons.core.logging.dto.RestResponseLog;
import com.demo.poc.commons.core.logging.enums.LoggingType;
import com.demo.poc.commons.core.restclient.utils.HeadersExtractor;
import com.demo.poc.commons.core.tracing.enums.TraceParam;
import com.demo.poc.commons.custom.properties.ApplicationProperties;
import jakarta.ws.rs.client.ClientRequestContext;
import jakarta.ws.rs.client.ClientResponseContext;
import jakarta.ws.rs.client.ClientResponseFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class RestClientResponseInterceptor implements ClientResponseFilter {

  private final ApplicationProperties properties;
  private final ThreadContextInjector contextInjector;

  @Override
  public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext) {
    generateTrace(requestContext, responseContext);
  }

  private void generateTrace(ClientRequestContext requestContext, ClientResponseContext responseContext) {
    boolean isLoggerPresent = LoggingType.isLoggerPresent(properties, LoggingType.REST_CLIENT_REQ);
    if (isLoggerPresent) {

      RestResponseLog log = RestResponseLog.builder()
          .uri(requestContext.getUri().toString())
          .responseBody("{\"to\":\"do\"}")
          .responseHeaders(HeadersExtractor.extractHeadersAsMap(responseContext.getHeaders()))
          .httpCode(String.valueOf(responseContext.getStatus()))
          .traceParent(HeadersExtractor.extractHeadersAsMap(requestContext.getHeaders()).get(TraceParam.TRACE_PARENT.getKey()))
          .build();

      contextInjector.populateFromRestResponse(LoggingType.REST_CLIENT_RES, log);
    }
  }
}