package com.demo.bbq.utils.tracing.logging.constants;

import javax.net.ssl.SSLException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.Map;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoggingMessage {

  public static final String HTTP_SUCCESSFUL_REQUEST = "HTTP Successful Request";

  public static final String HTTP_SUCCESSFUL_RESPONSE = "HTTP Successful Response";

  public static final String HTTP_ERROR_REQUEST = "HTTP Request Error - Exception class: ";

  public static final String HTTP_ERROR_RESPONSE = "HTTP Response Error - Exception class: ";

  public static final String EXCEPTION_WITHOUT_MESSAGE = "Exception without message";

  public static final Map<Class<? extends Throwable>, String> BASE_EXCEPTION_MESSAGES = Map.of(
      ConnectException.class, "Failed to connect to the server",
      UnknownHostException.class, "Could not resolve the server's address",
      SSLException.class, "SSL handshake failed"
  );
}
