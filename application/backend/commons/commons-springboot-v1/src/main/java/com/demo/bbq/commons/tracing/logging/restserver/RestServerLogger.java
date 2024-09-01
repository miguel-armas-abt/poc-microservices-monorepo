package com.demo.bbq.commons.tracing.logging.restserver;

import static com.demo.bbq.commons.tracing.logging.enums.LoggerType.REST_SERVER_REQ;
import static com.demo.bbq.commons.tracing.logging.restserver.RestServerLogger.RequestLoggerUtil.*;
import static com.demo.bbq.commons.tracing.logging.restserver.RestServerLogger.ResponseLoggerUtil.extractResponseHeadersAsMap;

import com.demo.bbq.commons.properties.ConfigurationBaseProperties;
import com.demo.bbq.commons.tracing.logging.enums.LoggerType;
import com.demo.bbq.commons.tracing.logging.injector.ThreadContextInjector;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
@WebFilter(urlPatterns = "/*")
@RequiredArgsConstructor
public class RestServerLogger implements Filter {

  private final ThreadContextInjector threadContextInjector;
  private final ConfigurationBaseProperties properties;

  @Override
  public void init(FilterConfig filterConfig) {
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    HttpServletRequest httpRequest = (HttpServletRequest) request;
    BufferingHttpServletResponse bufferingResponse = new BufferingHttpServletResponse((HttpServletResponse) response);
    generateTraceOfRequest(httpRequest);
    chain.doFilter(request, bufferingResponse);
    String responseBody = bufferingResponse.getCachedBody();
    generateTraceOfResponse(bufferingResponse, responseBody);
    response.getOutputStream().write(responseBody.getBytes(StandardCharsets.UTF_8));
  }

  @Override
  public void destroy() {
  }

  private void generateTraceOfRequest(HttpServletRequest request) {
    if (properties.isLoggerPresent(REST_SERVER_REQ)) {
      threadContextInjector.populateFromRestServerRequest(
          request.getMethod(),
          extractRequestURL(request),
          extractRequestHeadersAsMap(request),
          extractRequestBody(request));
    }
  }

  private void generateTraceOfResponse(HttpServletResponse response, String responseBody) {
    if (properties.isLoggerPresent(LoggerType.REST_SERVER_RES)) {
      threadContextInjector.populateFromRestServerResponse(
          extractResponseHeadersAsMap(response),
          responseBody,
          String.valueOf(response.getStatus()));
    }
  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static class RequestLoggerUtil {
    public static Map<String, String> extractRequestHeadersAsMap(HttpServletRequest request) {
      return Optional.ofNullable(request.getHeaderNames())
          .map(Collections::list)
          .orElseGet(ArrayList::new)
          .stream()
          .collect(Collectors.toMap(headerName -> headerName, request::getHeader));
    }

    public static String extractRequestBody(HttpServletRequest request) {
      try {
        return new BufferingHttpServletRequest(request).getRequestBody();
      } catch (IOException exception) {
        log.error("Error reading request body: {}", exception.getClass(), exception);
        return StringUtils.EMPTY;
      }
    }

    public static String extractRequestURL(HttpServletRequest request) {
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

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static class ResponseLoggerUtil {
    public static Map<String, String> extractResponseHeadersAsMap(HttpServletResponse response) {
      Map<String, String> headers = new HashMap<>();
      Collection<String> headerNames = response.getHeaderNames();
      headerNames.forEach(headerName -> headers.put(headerName, response.getHeader(headerName)));
      return headers;
    }
  }
}
