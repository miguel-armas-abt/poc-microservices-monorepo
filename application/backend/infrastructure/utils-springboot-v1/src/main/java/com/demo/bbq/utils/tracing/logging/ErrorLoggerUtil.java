package com.demo.bbq.utils.tracing.logging;

import static com.demo.bbq.utils.tracing.logging.constants.LoggingMessage.BASE_EXCEPTION_MESSAGES;
import static com.demo.bbq.utils.tracing.logging.constants.LoggingMessage.EXCEPTION_WITHOUT_MESSAGE;

import com.demo.bbq.utils.tracing.logging.injector.ThreadContextInjectorUtil;
import com.demo.bbq.utils.tracing.logging.util.HeaderMapperUtil;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.context.request.WebRequest;

@Slf4j
public class ErrorLoggerUtil {

  public static void generateTrace(Throwable ex, WebRequest request) {
    ThreadContextInjectorUtil.populateFromHeaders(HeaderMapperUtil.recoverTraceHeaders(request));
    String message = Optional.ofNullable(ex.getMessage()).orElse(EXCEPTION_WITHOUT_MESSAGE);

    if (ex instanceof ResourceAccessException resourceAccessException) {
      Throwable cause = resourceAccessException.getCause();
      message = BASE_EXCEPTION_MESSAGES.getOrDefault(cause.getClass(), Optional.ofNullable(cause.getMessage()).orElse("ResourceAccessException with an unspecified cause"));
    }

    log.error(message, ex);
    ThreadContext.clearAll();
  }
}
