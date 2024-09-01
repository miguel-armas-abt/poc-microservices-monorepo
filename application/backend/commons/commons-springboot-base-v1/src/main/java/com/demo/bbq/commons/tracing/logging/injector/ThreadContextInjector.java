package com.demo.bbq.commons.tracing.logging.injector;

import static com.demo.bbq.commons.tracing.logging.constants.ThreadContextConstant.*;
import static com.demo.bbq.commons.tracing.logging.enums.LoggerType.*;
import static com.demo.bbq.commons.tracing.logging.injector.ThreadContextInjector.LoggerUtil.extractTraceHeaders;

import com.demo.bbq.commons.properties.ConfigurationBaseProperties;
import com.demo.bbq.commons.properties.dto.obfuscation.ObfuscationTemplate;
import com.demo.bbq.commons.tracing.logging.obfuscation.body.BodyObfuscator;
import com.demo.bbq.commons.tracing.logging.obfuscation.header.HeaderObfuscator;
import jakarta.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.ThreadContext;

@Slf4j
@RequiredArgsConstructor
public class ThreadContextInjector {

  private final ConfigurationBaseProperties properties;
  private ObfuscationTemplate obfuscation;

  @PostConstruct
  public void init() {
    this.obfuscation = properties.getObfuscation();
  }

  private static void putInContext(String key, String value) {
    ThreadContext.put(key, StringUtils.defaultString(value));
  }

  public void populateFromTraceHeaders(Map<String, String> traceHeaders) {
    traceHeaders.forEach(ThreadContextInjector::putInContext);
  }

  public void populateFromRestClientRequest(String method, String uri, Map<String, String> headers, String body) {
    populateFromRestRequest(REST_CLIENT_REQ.getCode(), method, uri, headers, body);
    log.info(REST_CLIENT_REQ.getMessage());
    ThreadContext.clearAll();
  }

  public void populateFromRestClientResponse(Map<String, String> headers, String body, String httpCode) {
    populateFromRestResponse(REST_CLIENT_RES.getCode(), headers, body, httpCode);
    log.info(REST_CLIENT_RES.getMessage());
    ThreadContext.clearAll();
  }

  public void populateFromRestServerRequest(String method, String uri, Map<String, String> headers, String body) {
    populateFromRestRequest(REST_SERVER_REQ.getCode(), method, uri, headers, body);
    log.info(REST_SERVER_REQ.getMessage());
    ThreadContext.clearAll();
  }

  public void populateFromRestServerResponse(Map<String, String> headers, String body, String httpCode) {
    populateFromRestResponse(REST_SERVER_RES.getCode(), headers, body, httpCode);
    log.info(REST_SERVER_RES.getMessage());
    ThreadContext.clearAll();
  }

  private void populateFromRestRequest(String prefix, String method, String uri, Map<String, String> headers, String body) {
    populateFromTraceHeaders(extractTraceHeaders(headers));
    putInContext(prefix + METHOD, method);
    putInContext(prefix + URI, uri);
    putInContext(prefix + HEADERS, HeaderObfuscator.process(obfuscation, headers));
    putInContext(prefix + BODY, BodyObfuscator.process(obfuscation, body));
  }

  private void populateFromRestResponse(String prefix, Map<String, String> headers, String body, String httpCode) {
    populateFromTraceHeaders(extractTraceHeaders(headers));
    putInContext(prefix + HEADERS, HeaderObfuscator.process(obfuscation, headers));
    putInContext(prefix + BODY, BodyObfuscator.process(obfuscation, body));
    putInContext(prefix + STATUS, httpCode);
  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static class LoggerUtil {

    public static Map<String, String> extractTraceHeaders(Map<String, String> headers) {
      return Arrays.stream(TRACE_FIELDS)
          .map(traceField -> Map.entry(traceField, Optional.ofNullable(headers.get(traceField))))
          .filter(entry -> entry.getValue().isPresent())
          .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().get()));
    }

  }
}
