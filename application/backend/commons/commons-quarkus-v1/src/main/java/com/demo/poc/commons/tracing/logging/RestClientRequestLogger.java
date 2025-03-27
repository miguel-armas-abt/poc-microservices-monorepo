package com.demo.poc.commons.tracing.logging;

import static com.demo.poc.commons.tracing.logging.constants.LoggingMessage.REST_CLIENT_REQUEST;
import static com.demo.poc.commons.tracing.logging.injector.ThreadContextInjectorUtil.populateFromHeaders;
import static com.demo.poc.commons.tracing.logging.injector.ThreadContextInjectorUtil.populateFromRestClientRequest;

import com.demo.poc.commons.toolkit.HeaderPlanerUtil;
import jakarta.ws.rs.client.ClientRequestContext;
import jakarta.ws.rs.client.ClientRequestFilter;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RestClientRequestLogger implements ClientRequestFilter {

  @Override
  public void filter(ClientRequestContext requestContext) {
    String method = requestContext.getMethod();
    String uri = requestContext.getUri().toString();
    Map<String, String> requestHeaders = HeaderPlanerUtil.flatHeaders(requestContext.getHeaders());;

    populateFromHeaders(requestHeaders);
    populateFromRestClientRequest(method, uri, requestHeaders.toString(), "ToDo");
    log.info(REST_CLIENT_REQUEST);
  }
}