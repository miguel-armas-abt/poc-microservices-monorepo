package com.demo.bbq.commons.tracing.logging.error;

import static com.demo.bbq.commons.tracing.logging.constants.LoggingMessage.BASE_EXCEPTION_MESSAGES;
import static com.demo.bbq.commons.tracing.logging.constants.ThreadContextConstant.*;

import com.demo.bbq.commons.properties.ConfigurationBaseProperties;
import com.demo.bbq.commons.tracing.logging.enums.LoggerType;
import com.demo.bbq.commons.tracing.logging.injector.ThreadContextInjector;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.context.request.WebRequest;

@Slf4j
@RequiredArgsConstructor
public class ErrorLogger {

  private static final String UNDEFINED_MESSAGE = "Undefined";

  private final ThreadContextInjector threadContextInjector;
  private final ConfigurationBaseProperties properties;

  public void generateTraceIfLoggerIsPresent(Throwable ex, WebRequest request) {
    if(properties.isLoggerPresent(LoggerType.ERROR))
      generateTrace(ex, request);
  }

  private void generateTrace(Throwable exception, WebRequest request) {
    String message = Optional.ofNullable(exception.getMessage()).orElse(UNDEFINED_MESSAGE);

    if (exception instanceof ResourceAccessException resourceAccessException) {
      Throwable cause = resourceAccessException.getCause();
      message = BASE_EXCEPTION_MESSAGES.getOrDefault(cause.getClass(), Optional.ofNullable(cause.getMessage()).orElse(UNDEFINED_MESSAGE));
    }

    threadContextInjector.populateFromTraceHeaders(LoggerUtil.recoverTraceHeaders(request));
    log.error(message, exception);
    ThreadContext.clearAll();
  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  private static class LoggerUtil {

    public static Map<String, String> recoverTraceHeaders(WebRequest request) {
      return Arrays.stream(TRACE_FIELDS)
          .map(traceField -> Map.entry(traceField, Optional.ofNullable(request.getHeader(traceField))))
          .filter(entry -> entry.getValue().isPresent())
          .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().get()));
    }

  }
}