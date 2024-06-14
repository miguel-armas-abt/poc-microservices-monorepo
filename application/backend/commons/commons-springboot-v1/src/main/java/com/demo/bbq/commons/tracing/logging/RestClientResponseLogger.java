package com.demo.bbq.commons.tracing.logging;

import static com.demo.bbq.commons.tracing.logging.constants.LoggingMessage.REST_CLIENT_RESPONSE;
import static com.demo.bbq.commons.tracing.logging.constants.LoggingMessage.REST_CLIENT_RESPONSE_ERROR;

import com.demo.bbq.commons.properties.ConfigurationBaseProperties;
import com.demo.bbq.commons.tracing.logging.injector.ThreadContextInjectorUtil;
import com.demo.bbq.commons.tracing.logging.obfuscation.body.BodyObfuscatorUtil;
import com.demo.bbq.commons.tracing.logging.obfuscation.header.HeaderObfuscatorUtil;
import com.demo.bbq.commons.tracing.logging.wrapper.BufferingResponseWrapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;

@Slf4j
@RequiredArgsConstructor
public class RestClientResponseLogger implements ClientHttpRequestInterceptor {

  private final ConfigurationBaseProperties properties;

  @Override
  public ClientHttpResponse intercept(HttpRequest request, byte[] body,
                                      ClientHttpRequestExecution execution) throws IOException {
    ClientHttpResponse response = execution.execute(request, body);
    return decorateResponse(request, response);
  }

  public ClientHttpResponse decorateResponse(HttpRequest request, ClientHttpResponse response) throws IOException {
    BufferingResponseWrapper bufferedResponse = new BufferingResponseWrapper(response);
    String responseBody = StreamUtils.copyToString(bufferedResponse.getBody(), StandardCharsets.UTF_8);
    generateLog(properties, request, bufferedResponse, responseBody);
    return bufferedResponse;
  }

  private static void generateLog(ConfigurationBaseProperties properties,
                                  HttpRequest request,
                                  ClientHttpResponse response,
                                  String responseBody) {
    try {
      var method = request.getMethod().toString();
      var uri = request.getURI().toString();
      var headers = HeaderObfuscatorUtil.process(properties.getObfuscation(), response.getHeaders().toSingleValueMap());
      var body = BodyObfuscatorUtil.process(properties.getObfuscation(), responseBody);

      ThreadContextInjectorUtil.populateFromRestClientResponse(method, uri, headers, body, getHttpCode(response));
      log.info(REST_CLIENT_RESPONSE);

    } catch (Exception ex) {
      log.error(REST_CLIENT_RESPONSE_ERROR + ex.getClass(), ex);
    }
    ThreadContext.clearAll();
  }

  private static String getHttpCode(ClientHttpResponse response) throws IOException {
    try{
      return response.getStatusCode().toString();
    }catch (IllegalArgumentException | IOException ex){
      return String.valueOf(response.getStatusCode());
    }
  }
}
