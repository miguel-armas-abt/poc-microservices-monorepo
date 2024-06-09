package com.demo.bbq.config.tracing.logging;

import com.demo.bbq.config.tracing.logging.constants.LoggingMessage;
import com.demo.bbq.config.tracing.logging.injector.ThreadContextInjectorUtil;
import lombok.extern.slf4j.Slf4j;
import javax.inject.Singleton;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import javax.ws.rs.ext.Provider;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Provider
@Singleton
public class RestClientResponseLogger implements ClientResponseFilter {

  @Override
  public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext) {
    String method = requestContext.getMethod();
    String uri = requestContext.getUri().toString();
    String status = String.valueOf(responseContext.getStatus());
    Map<String, String> responseHeaders = responseContext.getHeaders()
        .entrySet()
        .stream()
        .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().get(0)));

    ThreadContextInjectorUtil.populateFromHeaders(responseHeaders);
    ThreadContextInjectorUtil.populateFromRestClientResponse(method, uri, responseHeaders.toString(), "ToDo", status);
    log.info(LoggingMessage.REST_CLIENT_RESPONSE);
  }
}