package com.demo.bbq.commons.core.interceptor.restclient.response;

import com.demo.bbq.commons.core.properties.ConfigurationBaseProperties;
import com.demo.bbq.commons.core.logging.ThreadContextInjector;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static com.demo.bbq.commons.core.logging.enums.LoggingType.REST_CLIENT_RES;

@Slf4j
@RequiredArgsConstructor
public class RestClientResponseInterceptor implements ClientHttpRequestInterceptor {

  private final ThreadContextInjector threadContextInjector;
  private final ConfigurationBaseProperties properties;

  @Override
  public ClientHttpResponse intercept(HttpRequest request, byte[] body,
                                      ClientHttpRequestExecution execution) throws IOException {
    ClientHttpResponse response = execution.execute(request, body);
    return generateTraceIfLoggerIsPresent(response);
  }

  private ClientHttpResponse generateTraceIfLoggerIsPresent(ClientHttpResponse response) throws IOException {
    if (properties.isLoggerPresent(REST_CLIENT_RES)) {
      return generateTrace(response);
    }
    return response;
  }

  private ClientHttpResponse generateTrace(ClientHttpResponse response) {
    try {
      BufferingClientHttpResponse bufferedResponse = new BufferingClientHttpResponse(response);
      String responseBody = StreamUtils.copyToString(bufferedResponse.getBody(), StandardCharsets.UTF_8);

      threadContextInjector.populateFromRestClientResponse(
          response.getHeaders().toSingleValueMap(),
          responseBody,
          getHttpCode(response));
      return bufferedResponse;

    } catch (Exception exception) {
      log.error("Error reading response body: {}", exception.getClass(), exception);
    }
    return response;
  }

  private static String getHttpCode(ClientHttpResponse response) throws IOException {
    try {
      return response.getStatusCode().toString();
    } catch (IOException ex) {
      return String.valueOf(response.getStatusCode());
    }
  }
}
