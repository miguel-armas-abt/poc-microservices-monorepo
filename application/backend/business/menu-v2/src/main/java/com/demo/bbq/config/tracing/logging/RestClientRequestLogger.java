package com.demo.bbq.config.tracing.logging;

import static com.demo.bbq.config.tracing.logging.constants.LoggingMessage.REST_CLIENT_REQUEST;
import static com.demo.bbq.config.tracing.logging.injector.ThreadContextInjectorUtil.populateFromHeaders;
import static com.demo.bbq.config.tracing.logging.injector.ThreadContextInjectorUtil.populateFromRestClientRequest;

import javax.inject.Singleton;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.ext.Provider;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Provider
@Singleton
public class RestClientRequestLogger implements ClientRequestFilter {

  @Override
  public void filter(ClientRequestContext requestContext) {
    String method = requestContext.getMethod();
    String uri = requestContext.getUri().toString();
    Map<String, String> requestHeaders = requestContext.getHeaders()
        .entrySet()
        .stream()
        .collect(Collectors.toMap(Map.Entry::getKey, entry -> (String) entry.getValue().get(0)));

    populateFromHeaders(requestHeaders);
    populateFromRestClientRequest(method, uri, requestHeaders.toString(), "ToDo");
    log.info(REST_CLIENT_REQUEST);
  }
}