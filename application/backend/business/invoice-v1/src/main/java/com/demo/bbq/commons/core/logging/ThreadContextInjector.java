package com.demo.bbq.commons.core.logging;

import com.demo.bbq.commons.core.obfuscation.body.BodyObfuscator;
import com.demo.bbq.commons.core.obfuscation.header.HeaderObfuscator;
import com.demo.bbq.commons.core.properties.ConfigurationBaseProperties;
import com.demo.bbq.commons.core.properties.obfuscation.ObfuscationTemplate;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.server.ServerWebExchange;

import java.util.Map;
import java.util.Optional;

import static com.demo.bbq.commons.core.logging.constants.RestLoggingConstant.BODY;
import static com.demo.bbq.commons.core.logging.constants.RestLoggingConstant.HEADERS;
import static com.demo.bbq.commons.core.logging.constants.RestLoggingConstant.METHOD;
import static com.demo.bbq.commons.core.logging.constants.RestLoggingConstant.STATUS;
import static com.demo.bbq.commons.core.logging.constants.RestLoggingConstant.URI;
import static com.demo.bbq.commons.core.logging.enums.LoggingType.REST_CLIENT_REQ;
import static com.demo.bbq.commons.core.logging.enums.LoggingType.REST_CLIENT_RES;
import static com.demo.bbq.commons.core.logging.enums.LoggingType.REST_SERVER_REQ;
import static com.demo.bbq.commons.core.logging.enums.LoggingType.REST_SERVER_RES;
import static com.demo.bbq.commons.core.tracing.utils.TraceHeaderExtractor.extractTraceHeadersAsMap;

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

  public void populateFromException(Throwable ex, ServerWebExchange exchange) {
    String message = Optional.ofNullable(ex.getMessage()).orElse("Undefined error message");

    if (ex instanceof WebClientRequestException webClientRequestException) {
      ThreadContext.put(REST_CLIENT_REQ.getCode() + METHOD, webClientRequestException.getMethod().toString());
      ThreadContext.put(REST_CLIENT_REQ.getCode() + URI, webClientRequestException.getUri().toString());
    }

    populateFromTraceHeaders(extractTraceHeadersAsMap(exchange.getRequest().getHeaders()::getFirst));
    log.error(message, ex);
    ThreadContext.clearAll();
  }

  private void populateFromRestRequest(String prefix, String method, String uri, Map<String, String> headers, String body) {
    populateFromTraceHeaders(extractTraceHeadersAsMap(headers::get));
    putInContext(prefix + METHOD, method);
    putInContext(prefix + URI, uri);
    putInContext(prefix + HEADERS, HeaderObfuscator.process(obfuscation, headers));
    putInContext(prefix + BODY, BodyObfuscator.process(obfuscation, body));
  }

  private void populateFromRestResponse(String prefix, Map<String, String> headers, String body, String httpCode) {
    populateFromTraceHeaders(extractTraceHeadersAsMap(headers::get));
    putInContext(prefix + HEADERS, HeaderObfuscator.process(obfuscation, headers));
    putInContext(prefix + BODY, BodyObfuscator.process(obfuscation, body));
    putInContext(prefix + STATUS, httpCode);
  }

}
