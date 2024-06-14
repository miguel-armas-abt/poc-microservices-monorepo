package com.demo.bbq.commons.tracing.logging;

import static com.demo.bbq.commons.tracing.logging.constants.LoggingMessage.REST_CLIENT_REQUEST_ERROR;
import static com.demo.bbq.commons.tracing.logging.constants.LoggingMessage.REST_CLIENT_REQUEST;

import com.demo.bbq.commons.properties.ConfigurationBaseProperties;
import com.demo.bbq.commons.tracing.logging.injector.ThreadContextInjectorUtil;
import com.demo.bbq.commons.tracing.logging.obfuscation.body.BodyObfuscatorUtil;
import com.demo.bbq.commons.tracing.logging.obfuscation.header.HeaderObfuscatorUtil;
import com.demo.bbq.commons.tracing.logging.util.HeaderMapperUtil;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

@Slf4j
@RequiredArgsConstructor
public class RestClientRequestLogger implements ClientHttpRequestInterceptor {

  private final ConfigurationBaseProperties properties;

  @Override
  public ClientHttpResponse intercept(HttpRequest request, byte[] body,
                                      ClientHttpRequestExecution execution) throws IOException {
    generateLog(properties, request, new String(body, StandardCharsets.UTF_8));
    return execution.execute(request, body);
  }

  private static void generateLog(ConfigurationBaseProperties properties,
                                  HttpRequest request,
                                  String requestBody) {
    try {
      var method = request.getMethod().toString();
      var uri = request.getURI().toString();
      var httpHeaders = request.getHeaders();
      var headers = HeaderObfuscatorUtil.process(properties.getObfuscation(), httpHeaders.toSingleValueMap());
      var body = BodyObfuscatorUtil.process(properties.getObfuscation(), requestBody);

      ThreadContextInjectorUtil.populateFromHeaders(HeaderMapperUtil.recoverTraceHeaders(httpHeaders));
      ThreadContextInjectorUtil.populateFromRestClientRequest(method, uri, headers, body);
      log.info(REST_CLIENT_REQUEST);

    } catch (Exception ex) {
      log.error(REST_CLIENT_REQUEST_ERROR + ex.getClass(), ex);
    }
    ThreadContext.clearAll();
  }

}
