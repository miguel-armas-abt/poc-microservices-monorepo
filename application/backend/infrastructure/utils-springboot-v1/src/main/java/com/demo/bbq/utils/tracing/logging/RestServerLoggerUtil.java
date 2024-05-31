package com.demo.bbq.utils.tracing.logging;

import com.demo.bbq.utils.properties.ConfigurationBaseProperties;
import com.demo.bbq.utils.tracing.logging.constants.LoggingMessage;
import com.demo.bbq.utils.tracing.logging.injector.ThreadContextInjectorUtil;
import com.demo.bbq.utils.tracing.logging.obfuscation.body.BodyObfuscatorUtil;
import com.demo.bbq.utils.tracing.logging.obfuscation.header.HeaderObfuscatorUtil;
import com.demo.bbq.utils.tracing.logging.obfuscation.header.strategy.HeaderObfuscationStrategy;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RestServerLoggerUtil {

  public static void decorateRequest(ConfigurationBaseProperties properties,
                              List<HeaderObfuscationStrategy> headerObfuscationStrategies,
                              HttpServletRequest request) {
    String method = request.getMethod();
    String url = getFullRequestURL(request);
    String requestBody = BodyObfuscatorUtil.process(properties.getObfuscation(), extractRequestBody(request));
    Map<String, String> requestHeaders = extractRequestHeaders(request);
    String obfuscatedHeaders = HeaderObfuscatorUtil.process(properties.getObfuscation(), headerObfuscationStrategies, requestHeaders);

    ThreadContextInjectorUtil.populateFromHeaders(requestHeaders);
    ThreadContextInjectorUtil.populateFromRestServerRequest(method, url, obfuscatedHeaders, requestBody);
    log.info(LoggingMessage.REST_SERVER_REQUEST);
  }

  public static void decorateResponse(ConfigurationBaseProperties properties,
                               List<HeaderObfuscationStrategy> headerObfuscationStrategies,
                               HttpServletRequest request,
                               HttpServletResponse response) {
    String method = request.getMethod();
    String url = getFullRequestURL(request);
    Map<String, String> responseHeaders = extractResponseHeaders(response);
    String obfuscatedHeaders = HeaderObfuscatorUtil.process(properties.getObfuscation(), headerObfuscationStrategies, responseHeaders);
    String httpCode = String.valueOf(response.getStatus());

    ThreadContextInjectorUtil.populateFromHeaders(responseHeaders);
    ThreadContextInjectorUtil.populateFromRestServerResponse(method, url, obfuscatedHeaders, StringUtils.EMPTY, httpCode);
    log.info(LoggingMessage.REST_SERVER_RESPONSE);
  }

  private static String extractRequestBody(HttpServletRequest request) {
    StringBuilder stringBuilder = new StringBuilder();
    try (BufferedReader bufferedReader = request.getReader()) {
      bufferedReader.lines().forEach(stringBuilder::append);
    } catch (IOException e) {
      log.error("Error reading request body", e);
    }
    return stringBuilder.toString();
  }

  private static Map<String, String> extractRequestHeaders(HttpServletRequest request) {
    Map<String, String> headers = new HashMap<>();
    Enumeration<String> headerNames = request.getHeaderNames();
    while (headerNames.hasMoreElements()) {
      String headerName = headerNames.nextElement();
      headers.put(headerName, request.getHeader(headerName));
    }
    return headers;
  }

  private static Map<String, String> extractResponseHeaders(HttpServletResponse response) {
    Map<String, String> headers = new HashMap<>();
    Collection<String> headerNames = response.getHeaderNames();
    headerNames.forEach(headerName -> headers.put(headerName, response.getHeader(headerName)));
    return headers;
  }

  private static String getFullRequestURL(HttpServletRequest request) {
    String url = Optional.of(request)
        .map(HttpServletRequest::getRequestURL)
        .map(StringBuffer::toString)
        .orElse(StringUtils.EMPTY);

    String queryString = request.getQueryString();
    if (StringUtils.isNotBlank(queryString))
      url += "?" + queryString;

    return url;
  }
}
