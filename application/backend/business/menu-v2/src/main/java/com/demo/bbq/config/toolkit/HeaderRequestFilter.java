package com.demo.bbq.config.toolkit;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;
import java.util.Map;

public class HeaderRequestFilter implements ClientRequestFilter {

  private final Map<String, String> headers;

  public HeaderRequestFilter(Map<String, String> headers) {
    this.headers = headers;
  }

  @Override
  public void filter(ClientRequestContext requestContext) throws IOException {
    MultivaluedMap<String, Object> requestHeaders = requestContext.getHeaders();
    headers.forEach(requestHeaders::add);
  }
}
