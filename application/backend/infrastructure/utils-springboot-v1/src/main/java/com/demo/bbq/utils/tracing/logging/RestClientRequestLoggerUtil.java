package com.demo.bbq.utils.tracing.logging;

import static com.demo.bbq.utils.tracing.logging.constants.LoggingMessage.HTTP_ERROR_REQUEST;
import static com.demo.bbq.utils.tracing.logging.constants.LoggingMessage.HTTP_REQUEST;

import com.demo.bbq.utils.properties.ConfigurationBaseProperties;
import com.demo.bbq.utils.tracing.logging.injector.ThreadContextInjectorUtil;
import com.demo.bbq.utils.tracing.logging.obfuscation.body.BodyObfuscatorUtil;
import com.demo.bbq.utils.tracing.logging.obfuscation.header.HeaderObfuscatorUtil;
import com.demo.bbq.utils.tracing.logging.obfuscation.header.strategy.HeaderObfuscationStrategy;
import com.demo.bbq.utils.tracing.logging.util.HeaderMapperUtil;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpResponse;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RestClientRequestLoggerUtil {

  public static ClientHttpResponse decorateRequest(ConfigurationBaseProperties properties,
                                                   List<HeaderObfuscationStrategy> headerObfuscationStrategies,
                                                   HttpRequest request,
                                                   byte[] body,
                                                   ClientHttpRequestExecution execution) throws IOException {
    generateLog(properties, headerObfuscationStrategies, request, new String(body, StandardCharsets.UTF_8));
    return execution.execute(request, body);
  }

  private static void generateLog(ConfigurationBaseProperties properties,
                                  List<HeaderObfuscationStrategy> headerObfuscationStrategies,
                                  HttpRequest request,
                                  String requestBody) {
    try {
      var method = request.getMethod().toString();
      var uri = request.getURI().toString();
      var httpHeaders = request.getHeaders();
      var headers = HeaderObfuscatorUtil.process(properties.getObfuscation(), headerObfuscationStrategies, httpHeaders.toSingleValueMap());
      var body = BodyObfuscatorUtil.process(properties.getObfuscation(), requestBody);

      ThreadContextInjectorUtil.populateFromHeaders(HeaderMapperUtil.recoverTraceHeaders(httpHeaders));
      ThreadContextInjectorUtil.populateFromClientRequest(method, uri, headers, body);
      log.info(HTTP_REQUEST);

    } catch (Exception ex) {
      log.error(HTTP_ERROR_REQUEST + ex.getClass(), ex);
    }
    ThreadContext.clearAll();
  }

}
