package com.demo.poc.commons.core.interceptor.restclient;

import com.demo.poc.commons.core.logging.ThreadContextInjector;
import com.demo.poc.commons.core.restclient.utils.HeadersExtractor;
import jakarta.ws.rs.client.ClientRequestContext;
import jakarta.ws.rs.client.ClientRequestFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class RestClientRequestInterceptor implements ClientRequestFilter {

  private final ThreadContextInjector threadContextInjector;

  @Override
  public void filter(ClientRequestContext requestContext) {
    String method = requestContext.getMethod();
    String uri = requestContext.getUri().toString();
    Map<String, String> requestHeaders = HeadersExtractor.extractHeadersAsMap(requestContext.getHeaders());;

    threadContextInjector.populateFromRestClientRequest(method, uri, requestHeaders, "{\"to\":\"do\"}");
  }
}