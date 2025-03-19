package com.demo.bbq.commons.interceptor.restclient.request;

import com.demo.bbq.commons.properties.base.ConfigurationBaseProperties;
import com.demo.bbq.commons.logging.ThreadContextInjector;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static com.demo.bbq.commons.logging.enums.LoggingType.REST_CLIENT_REQ;

@Slf4j
@RequiredArgsConstructor
public class RestClientRequestInterceptor implements ClientHttpRequestInterceptor {

  private final ThreadContextInjector threadContextInjector;
  private final ConfigurationBaseProperties properties;

  @Override
  public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
    generateTraceIfLoggerIsPresent(request, body);
    return execution.execute(request, body);
  }

  private void generateTraceIfLoggerIsPresent(HttpRequest request, byte[] body) {
    if (properties.isLoggerPresent(REST_CLIENT_REQ))
      generateTrace(request, new String(body, StandardCharsets.UTF_8));
  }

  private void generateTrace(HttpRequest request, String requestBody) {
    try {
      threadContextInjector.populateFromRestClientRequest(
          request.getMethod().toString(),
          request.getURI().toString(),
          request.getHeaders().toSingleValueMap(),
          requestBody
      );
    } catch (Exception exception) {
      log.error("Error reading request body {}", exception.getClass(), exception);
    }
  }

}