package com.demo.poc.commons.core.logging;

import com.demo.poc.commons.core.errors.exceptions.NoSuchLoggingTemplateException;
import com.demo.poc.commons.core.errors.exceptions.NoSuchObfuscationTemplateException;
import com.demo.poc.commons.core.logging.constants.RestLoggingConstant;
import com.demo.poc.commons.core.logging.enums.LoggingType;
import com.demo.poc.commons.core.logging.obfuscation.body.BodyObfuscator;
import com.demo.poc.commons.core.logging.obfuscation.header.HeaderObfuscator;
import com.demo.poc.commons.core.properties.ConfigurationBaseProperties;
import com.demo.poc.commons.core.properties.logging.ObfuscationTemplate;
import com.demo.poc.commons.core.tracing.utils.TraceHeaderExtractor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

import java.util.Map;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class ThreadContextInjector {

  private final ConfigurationBaseProperties properties;

  private static void putInContext(String key, String value) {
    MDC.put(key, StringUtils.defaultString(value));
  }

  public static void populateFromTraceHeaders(Map<String, String> traceHeaders) {
    traceHeaders.forEach(ThreadContextInjector::putInContext);
  }

  public void populateFromRestClientRequest(String method, String uri, Map<String, String> headers, String body) {
    populateFromRestRequest(LoggingType.REST_CLIENT_REQ.getCode(), method, uri, headers, body);
    log.info(LoggingType.REST_CLIENT_REQ.getMessage());
    MDC.clear();
  }

  public void populateFromRestClientResponse(Map<String, String> headers, String uri, String body, String httpCode) {
    populateFromRestResponse(LoggingType.REST_CLIENT_RES.getCode(), uri, headers, body, httpCode);
    log.info(LoggingType.REST_CLIENT_RES.getMessage());
    MDC.clear();
  }

  public void populateFromRestServerRequest(String method, String uri, Map<String, String> headers, String body) {
    populateFromRestRequest(LoggingType.REST_SERVER_REQ.getCode(), method, uri, headers, body);
    log.info(LoggingType.REST_SERVER_REQ.getMessage());
    MDC.clear();
  }

  public void populateFromRestServerResponse(Map<String, String> headers, String uri, String body, String httpCode) {
    populateFromRestResponse(LoggingType.REST_SERVER_RES.getCode(), uri, headers, body, httpCode);
    log.info(LoggingType.REST_SERVER_RES.getMessage());
    MDC.clear();
  }

  public void populateFromException(Throwable exception) {
    String message = Optional.ofNullable(exception.getMessage()).orElse("Undefined error message");
    log.error(message, exception);
    MDC.clear();
  }

  private void populateFromRestRequest(String prefix, String method, String uri, Map<String, String> headers, String body) {
    populateFromTraceHeaders(TraceHeaderExtractor.extractTraceHeadersAsMap(headers::get));
    putInContext(prefix + RestLoggingConstant.METHOD, method);
    putInContext(prefix + RestLoggingConstant.URI, uri);
    putInContext(prefix + RestLoggingConstant.HEADERS, HeaderObfuscator.process(getObfuscationTemplate(), headers));
    putInContext(prefix + RestLoggingConstant.BODY, BodyObfuscator.process(getObfuscationTemplate(), body));
  }

  private void populateFromRestResponse(String prefix, String uri, Map<String, String> headers, String body, String httpCode) {
    populateFromTraceHeaders(TraceHeaderExtractor.extractTraceHeadersAsMap(headers::get));
    putInContext(prefix + RestLoggingConstant.URI, uri);
    putInContext(prefix + RestLoggingConstant.HEADERS, HeaderObfuscator.process(getObfuscationTemplate(), headers));
    putInContext(prefix + RestLoggingConstant.BODY, BodyObfuscator.process(getObfuscationTemplate(), body));
    putInContext(prefix + RestLoggingConstant.STATUS, httpCode);
  }

  ObfuscationTemplate getObfuscationTemplate() {
    return properties.logging()
            .map(loggingTemplate -> loggingTemplate.obfuscation().orElseThrow(NoSuchObfuscationTemplateException::new))
            .orElseThrow(NoSuchLoggingTemplateException::new);
  }
}
