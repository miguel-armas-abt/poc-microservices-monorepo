package com.demo.bbq.commons.tracing.logging;

import static com.demo.bbq.commons.tracing.logging.constants.LoggingMessage.EXCEPTION_WITHOUT_MESSAGE;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

@Slf4j
public class ErrorLoggingUtil {

  public static void generateTrace(Throwable ex) {
    String message = Optional.ofNullable(ex.getMessage()).orElse(EXCEPTION_WITHOUT_MESSAGE);
    log.error(message, ex);
    MDC.clear();
  }
}
