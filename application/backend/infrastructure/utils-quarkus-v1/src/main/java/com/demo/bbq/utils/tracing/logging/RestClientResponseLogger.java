package com.demo.bbq.utils.tracing.logging;

import static com.demo.bbq.utils.tracing.logging.constants.LoggingMessage.REST_CLIENT_RESPONSE;
import static com.demo.bbq.utils.tracing.logging.injector.ThreadContextInjectorUtil.populateFromHeaders;
import static com.demo.bbq.utils.tracing.logging.injector.ThreadContextInjectorUtil.populateFromRestClientResponse;

import com.demo.bbq.utils.toolkit.HeaderPlanerUtil;
import lombok.extern.slf4j.Slf4j;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import java.util.Map;

@Slf4j
public class RestClientResponseLogger implements ClientResponseFilter {

  @Override
  public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext) {
    String method = requestContext.getMethod();
    String uri = requestContext.getUri().toString();
    String status = String.valueOf(responseContext.getStatus());
    Map<String, String> responseHeaders = HeaderPlanerUtil.flatHeaders(responseContext.getHeaders());

    populateFromHeaders(responseHeaders);
    populateFromRestClientResponse(method, uri, responseHeaders.toString(), "ToDo", status);
    log.info(REST_CLIENT_RESPONSE);
  }
}