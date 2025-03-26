package com.demo.bbq.commons.core.logging;

import com.demo.bbq.commons.core.logging.constants.RestLoggingConstant;
import com.demo.bbq.commons.core.logging.enums.LoggingType;
import com.demo.bbq.commons.core.properties.ConfigurationBaseProperties;
import com.demo.bbq.commons.core.properties.obfuscation.ObfuscationTemplate;
import com.demo.bbq.commons.core.obfuscation.body.BodyObfuscator;
import com.demo.bbq.commons.core.obfuscation.header.HeaderObfuscator;
import com.demo.bbq.commons.core.tracing.utils.TraceHeaderExtractor;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;
import java.util.Optional;

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
    populateFromRestRequest(LoggingType.REST_CLIENT_REQ.getCode(), method, uri, headers, body);
    log.info(LoggingType.REST_CLIENT_REQ.getMessage());
    ThreadContext.clearAll();
  }

  public void populateFromRestClientResponse(Map<String, String> headers, String body, String httpCode) {
    populateFromRestResponse(LoggingType.REST_CLIENT_RES.getCode(), headers, body, httpCode);
    log.info(LoggingType.REST_CLIENT_RES.getMessage());
    ThreadContext.clearAll();
  }

  public void populateFromRestServerRequest(String method, String uri, Map<String, String> headers, String body) {
    populateFromRestRequest(LoggingType.REST_SERVER_REQ.getCode(), method, uri, headers, body);
    log.info(LoggingType.REST_SERVER_REQ.getMessage());
    ThreadContext.clearAll();
  }

  public void populateFromRestServerResponse(Map<String, String> headers, String body, String httpCode) {
    populateFromRestResponse(LoggingType.REST_SERVER_RES.getCode(), headers, body, httpCode);
    log.info(LoggingType.REST_SERVER_RES.getMessage());
    ThreadContext.clearAll();
  }

  public void populateFromException(Throwable exception, WebRequest request) {
    String message = Optional.ofNullable(exception.getMessage()).orElse("Undefined error message");
    populateFromTraceHeaders(TraceHeaderExtractor.extractTraceHeadersAsMap(request::getHeader));
    log.error(message, exception);
    ThreadContext.clearAll();
  }

  private void populateFromRestRequest(String prefix, String method, String uri, Map<String, String> headers, String body) {
    populateFromTraceHeaders(TraceHeaderExtractor.extractTraceHeadersAsMap(headers::get));
    putInContext(prefix + RestLoggingConstant.METHOD, method);
    putInContext(prefix + RestLoggingConstant.URI, uri);
    putInContext(prefix + RestLoggingConstant.HEADERS, HeaderObfuscator.process(obfuscation, headers));
    putInContext(prefix + RestLoggingConstant.BODY, BodyObfuscator.process(obfuscation, body));
  }

  private void populateFromRestResponse(String prefix, Map<String, String> headers, String body, String httpCode) {
    populateFromTraceHeaders(TraceHeaderExtractor.extractTraceHeadersAsMap(headers::get));
    putInContext(prefix + RestLoggingConstant.HEADERS, HeaderObfuscator.process(obfuscation, headers));
    putInContext(prefix + RestLoggingConstant.BODY, BodyObfuscator.process(obfuscation, body));
    putInContext(prefix + RestLoggingConstant.STATUS, httpCode);
  }

}
