package com.demo.bbq.utils.tracing.logging;

import static com.demo.bbq.utils.tracing.logging.constants.LoggingMessage.HTTP_ERROR_RESPONSE;
import static com.demo.bbq.utils.tracing.logging.constants.LoggingMessage.HTTP_RESPONSE;

import com.demo.bbq.utils.properties.ConfigurationBaseProperties;
import com.demo.bbq.utils.tracing.logging.injector.ThreadContextInjectorUtil;
import com.demo.bbq.utils.tracing.logging.obfuscation.body.BodyObfuscatorUtil;
import com.demo.bbq.utils.tracing.logging.obfuscation.header.HeaderObfuscatorUtil;
import com.demo.bbq.utils.tracing.logging.obfuscation.header.strategy.HeaderObfuscationStrategy;
import com.demo.bbq.utils.tracing.logging.wrapper.BufferingResponseWrapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientResponseLoggingUtil {

  public static ClientHttpResponse decorateResponse(ConfigurationBaseProperties properties,
                                                    List<HeaderObfuscationStrategy> headerObfuscationStrategies,
                                                    HttpRequest request,
                                                    ClientHttpResponse response) throws IOException {

    BufferingResponseWrapper bufferedResponse = new BufferingResponseWrapper(response);
    String responseBody = StreamUtils.copyToString(bufferedResponse.getBody(), StandardCharsets.UTF_8);
    generateLog(properties, headerObfuscationStrategies, request, bufferedResponse, responseBody);
    return bufferedResponse;
  }

  private static void generateLog(ConfigurationBaseProperties properties,
                                  List<HeaderObfuscationStrategy> headerObfuscationStrategies,
                                  HttpRequest request,
                                  ClientHttpResponse response,
                                  String responseBody) {
    try {
      var method = request.getMethod().toString();
      var uri = request.getURI().toString();
      var headers = HeaderObfuscatorUtil.process(properties.getObfuscation(), headerObfuscationStrategies, response.getHeaders().toSingleValueMap());
      var body = BodyObfuscatorUtil.process(properties.getObfuscation(), responseBody);

      ThreadContextInjectorUtil.populateFromClientResponse(method, uri, headers, body, getHttpCode(response));
      log.info(HTTP_RESPONSE);

    } catch (Exception ex) {
      log.error(HTTP_ERROR_RESPONSE + ex.getClass(), ex);
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
