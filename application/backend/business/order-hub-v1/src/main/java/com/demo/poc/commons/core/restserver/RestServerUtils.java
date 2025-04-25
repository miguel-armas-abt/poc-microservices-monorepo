package com.demo.poc.commons.core.restserver;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.demo.poc.commons.core.tracing.enums.TraceParamType.TRACE_ID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RestServerUtils {

  public static Map<String, String> extractHeadersAsMap(ServerRequest serverRequest) {
    return Optional.of(serverRequest.headers().asHttpHeaders().toSingleValueMap())
        .map(Map::entrySet)
        .orElse(Collections.emptySet())
        .stream()
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }

  public static Map<String, String> extractHeadersAsMap(ServerHttpRequest serverHttpRequest) {
    return Optional.of(serverHttpRequest.getHeaders())
        .map(headers -> headers.toSingleValueMap().entrySet())
        .orElse(Collections.emptySet())
        .stream()
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }

  public static Map<String, String> extractQueryParamsAsMap(ServerRequest serverRequest) {
    return serverRequest.queryParams()
        .entrySet()
        .stream()
        .collect(Collectors.toMap(
            Map.Entry::getKey,
            entry -> String.join(",", entry.getValue())
        ));
  }

  public static Consumer<HttpHeaders> buildResponseHeaders(ServerRequest.Headers requestHeaders) {
    return currentHeaders -> Optional
        .ofNullable(requestHeaders.firstHeader(TRACE_ID.getKey()))
        .ifPresentOrElse(traceId -> currentHeaders.set(TRACE_ID.getKey(), traceId)
            , () -> {});
  }

}