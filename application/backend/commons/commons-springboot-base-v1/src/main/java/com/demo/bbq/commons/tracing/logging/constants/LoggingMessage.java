package com.demo.bbq.commons.tracing.logging.constants;

import javax.net.ssl.SSLException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.Map;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoggingMessage {

  public static final Map<Class<? extends Throwable>, String> BASE_EXCEPTION_MESSAGES = Map.of(
      ConnectException.class, "Failed to connect to the server",
      UnknownHostException.class, "Could not resolve the server's address",
      SSLException.class, "SSL handshake failed"
  );
}
