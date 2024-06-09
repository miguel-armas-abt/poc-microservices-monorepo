package com.demo.bbq.utils.tracing.logging;

import static com.demo.bbq.utils.tracing.logging.constants.LoggingMessage.REST_CLIENT_REQUEST;
import static com.demo.bbq.utils.tracing.logging.injector.ThreadContextInjectorUtil.populateFromHeaders;
import static com.demo.bbq.utils.tracing.logging.injector.ThreadContextInjectorUtil.populateFromRestClientRequest;

import com.demo.bbq.utils.toolkit.HeaderPlanerUtil;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
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