package com.demo.poc.commons.core.interceptor.restclient;

import com.demo.poc.commons.core.logging.ThreadContextInjector;
import com.demo.poc.commons.core.restclient.utils.HeadersExtractor;
import jakarta.ws.rs.client.ClientRequestContext;
import jakarta.ws.rs.client.ClientResponseContext;
import jakarta.ws.rs.client.ClientResponseFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class RestClientResponseInterceptor implements ClientResponseFilter {

  private final ThreadContextInjector threadContextInjector;

  @Override
  public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext) {
    String method = requestContext.getMethod();
    String uri = requestContext.getUri().toString();
    String status = String.valueOf(responseContext.getStatus());
    Map<String, String> responseHeaders = HeadersExtractor.extractHeadersAsMap(responseContext.getHeaders());

    threadContextInjector.populateFromRestClientResponse(responseHeaders, uri,"{\"to\":\"do\"}", status);
  }
}