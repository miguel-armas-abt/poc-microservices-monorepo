package com.demo.poc.commons.core.restserver.utils;

import com.demo.poc.commons.core.constants.Symbol;
import com.demo.poc.commons.core.tracing.enums.TraceParam;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RestServerUtils {

  public static Map<String, String> extractHeadersAsMap(ServerRequest serverRequest) {
    return Optional.of(serverRequest.headers().asHttpHeaders().toSingleValueMap())
        .map(Map::entrySet)
        .orElse(Collections.emptySet())
        .stream()
        .collect(Collectors.toMap(
            Map.Entry::getKey,
            Map.Entry::getValue,
            (u, v) -> v,
            () -> new TreeMap<>(String.CASE_INSENSITIVE_ORDER)
        ));
  }

  public static Map<String, String> extractHeadersAsMap(ServerHttpRequest serverHttpRequest) {
    return Optional.of(serverHttpRequest.getHeaders())
        .map(headers -> headers.toSingleValueMap().entrySet())
        .orElse(Collections.emptySet())
        .stream()
        .collect(Collectors.toMap(
            Map.Entry::getKey,
            Map.Entry::getValue,
            (u, v) -> v,
            () -> new TreeMap<>(String.CASE_INSENSITIVE_ORDER)
        ));
  }

  public static Map<String, String> extractQueryParamsAsMap(ServerRequest serverRequest) {
    return serverRequest.queryParams()
        .entrySet()
        .stream()
        .collect(Collectors.toMap(
            Map.Entry::getKey,
            entry -> String.join(Symbol.COMMA, entry.getValue())
        ));
  }

  public static Consumer<HttpHeaders> buildResponseHeaders(ServerRequest.Headers requestHeaders) {
    return responseHeaders -> Optional
        .ofNullable(requestHeaders.firstHeader(TraceParam.TRACE_PARENT.getKey()))
        .ifPresentOrElse(traceParent -> responseHeaders.set(TraceParam.TRACE_ID.getKey(), TraceParam.Util.getTraceId.apply(traceParent))
            , () -> {});
  }

}