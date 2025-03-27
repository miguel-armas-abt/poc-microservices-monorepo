package com.demo.poc.commons.tracing.logging.constants;

import javax.net.ssl.SSLException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.Map;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoggingMessage {

  public static final String REST_SERVER_REQUEST = "REST Server Request";
  public static final String REST_SERVER_RESPONSE = "REST Server Response";

  public static final String REST_CLIENT_REQUEST = "REST Client Request";
  public static final String REST_CLIENT_REQUEST_ERROR = "REST Client Request Error - Exception class: ";

  public static final String REST_CLIENT_RESPONSE = "REST Client Response";

  public static final String REST_CLIENT_RESPONSE_ERROR = "REST Client Response Error - Exception class: ";

  public static final String EXCEPTION_WITHOUT_MESSAGE = "Exception without message";

  public static final Map<Class<? extends Throwable>, String> BASE_EXCEPTION_MESSAGES = Map.of(
      ConnectException.class, "Failed to connect to the server",
      UnknownHostException.class, "Could not resolve the server's address",
      SSLException.class, "SSL handshake failed"
  );
}
