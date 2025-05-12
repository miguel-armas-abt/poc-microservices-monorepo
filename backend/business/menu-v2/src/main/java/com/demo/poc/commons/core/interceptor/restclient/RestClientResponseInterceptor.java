package com.demo.poc.commons.core.interceptor.restclient;

import com.demo.poc.commons.core.errors.exceptions.NoSuchLoggingTemplateException;
import com.demo.poc.commons.core.logging.ThreadContextInjector;
import com.demo.poc.commons.core.logging.enums.LoggingType;
import com.demo.poc.commons.core.properties.logging.LoggingTemplate;
import com.demo.poc.commons.core.restclient.utils.HeadersExtractor;
import com.demo.poc.commons.custom.properties.ApplicationProperties;
import jakarta.ws.rs.client.ClientRequestContext;
import jakarta.ws.rs.client.ClientResponseContext;
import jakarta.ws.rs.client.ClientResponseFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class RestClientResponseInterceptor implements ClientResponseFilter {

  private final ApplicationProperties properties;
  private final ThreadContextInjector threadContextInjector;

  @Override
  public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext) {
    generateTrace(requestContext, responseContext);
  }

  private void generateTrace(ClientRequestContext requestContext, ClientResponseContext responseContext) {
    LoggingTemplate loggingTemplate = properties.logging().orElseThrow(NoSuchLoggingTemplateException::new);
    if(LoggingType.isLoggerPresent(LoggingType.REST_CLIENT_RES, loggingTemplate.loggingType())) {

      String method = requestContext.getMethod();
      String uri = requestContext.getUri().toString();
      String status = String.valueOf(responseContext.getStatus());
      Map<String, String> responseHeaders = HeadersExtractor.extractHeadersAsMap(responseContext.getHeaders());

      threadContextInjector.populateFromRestClientResponse(responseHeaders, uri,"{\"to\":\"do\"}", status);
    }
  }
}