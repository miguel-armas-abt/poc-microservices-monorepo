package com.demo.poc.commons.core.restserver;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.Optional;
import java.util.function.Consumer;

import static com.demo.poc.commons.core.tracing.enums.TraceParamType.TRACE_ID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RestServerUtils {

  public static Consumer<HttpHeaders> buildResponseHeaders(ServerRequest.Headers requestHeaders) {
    return currentHeaders -> Optional
        .ofNullable(requestHeaders.firstHeader(TRACE_ID.getKey()))
        .ifPresentOrElse(traceId -> currentHeaders.set(TRACE_ID.getKey(), traceId)
            , () -> {});
  }

}