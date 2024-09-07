package com.demo.bbq.commons.tracing.logging.error;

import static com.demo.bbq.commons.tracing.logging.enums.LoggerType.REST_CLIENT_REQ;
import static com.demo.bbq.commons.tracing.logging.injector.ThreadContextUtils.*;

import com.demo.bbq.commons.properties.ConfigurationBaseProperties;
import com.demo.bbq.commons.tracing.logging.constants.ExceptionLoggingMessage;
import com.demo.bbq.commons.tracing.logging.enums.LoggerType;
import com.demo.bbq.commons.tracing.logging.injector.ThreadContextInjector;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.server.ServerWebExchange;

@Slf4j
@RequiredArgsConstructor
public class ErrorLogger {

  private static final String UNDEFINED_MESSAGE = "Undefined";

  private final ThreadContextInjector threadContextInjector;
  private final ConfigurationBaseProperties properties;

  public void generateTraceIfLoggerIsPresent(Throwable ex, ServerWebExchange exchange) {
    if(properties.isLoggerPresent(LoggerType.ERROR))
      generateTrace(ex, exchange);
  }

  private void generateTrace(Throwable ex, ServerWebExchange exchange) {
    String message = Optional.ofNullable(ex.getMessage()).orElse(UNDEFINED_MESSAGE);

    if (ex instanceof WebClientRequestException webClientRequestException) {
      ThreadContext.put(REST_CLIENT_REQ.getCode() + METHOD, webClientRequestException.getMethod().toString());
      ThreadContext.put(REST_CLIENT_REQ.getCode() + URI, webClientRequestException.getUri().toString());

      Throwable cause = webClientRequestException.getCause();
      message = ExceptionLoggingMessage
          .getExceptionMessages()
          .getOrDefault(cause.getClass(), Optional.ofNullable(cause.getMessage()).orElse(UNDEFINED_MESSAGE));
    }

    threadContextInjector.populateFromTraceHeaders(recoverTraceHeaders(exchange.getRequest()));
    log.error(message, ex);
    ThreadContext.clearAll();
  }

  private static Map<String, String> recoverTraceHeaders(ServerHttpRequest request) {
    return Arrays.stream(TRACE_HEADERS)
        .map(traceField -> Map.entry(traceField, Optional.ofNullable(request.getHeaders().getFirst(traceField))))
        .filter(entry -> entry.getValue().isPresent())
        .collect(Collectors.toMap(entry -> toCamelCase(entry.getKey()), entry -> entry.getValue().get()));
  }
}
