package com.demo.bbq.utils.interceptor.logging.mdc;

import static com.demo.bbq.utils.interceptor.logging.constants.LoggingConstant.MDC_REQ_METHOD;
import static com.demo.bbq.utils.interceptor.logging.constants.LoggingConstant.MDC_REQ_URI;

import io.netty.channel.ConnectTimeoutException;
import io.netty.handler.timeout.ReadTimeoutException;
import io.netty.handler.timeout.TimeoutException;
import javax.net.ssl.SSLException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.server.ServerWebExchange;

@Slf4j
public class ErrorTracingUtil {

  private static final Map<Class<? extends Throwable>, String> EXCEPTION_MESSAGES = Map.of(
      TimeoutException.class, "Timeout occurred while processing request",
      ConnectTimeoutException.class, "Connection timeout while attempting to connect to the server",
      ReadTimeoutException.class, "Read timeout occurred while waiting for the server's response",
      ConnectException.class, "Failed to connect to the server",
      UnknownHostException.class, "Could not resolve the server's address",
      SSLException.class, "SSL handshake failed"
  );

  public static void generateTrace(Throwable ex, ServerWebExchange exchange) {
    TracingGeneratorUtil.generateTrace(exchange.getRequest().getHeaders());
    String message = Optional.ofNullable(ex.getMessage()).orElse("Exception without specific message");

    if (ex instanceof WebClientRequestException webClientRequestException) {
      ThreadContext.put(MDC_REQ_METHOD, webClientRequestException.getMethod().toString());
      ThreadContext.put(MDC_REQ_URI, webClientRequestException.getUri().toString());

      Throwable cause = webClientRequestException.getCause();
      message = EXCEPTION_MESSAGES.getOrDefault(cause.getClass(),
          Optional.ofNullable(cause.getMessage()).orElse("WebClientRequestException with an unspecified cause"));
    }

    log.error(message, ex);
    ThreadContext.clearAll();
  }
}
