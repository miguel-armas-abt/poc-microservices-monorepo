package com.demo.poc.commons.core.restserver;

import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import jakarta.ws.rs.core.MultivaluedMap;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RestServerUtils {

  public static Map<String, String> extractHeadersAsMap(MultivaluedMap<String, String> headers) {
    return headers.keySet().stream()
        .collect(Collectors.toMap(
            Function.identity(),
            headers::getFirst,
            (v1, v2) -> v1,
            () -> new TreeMap<>(String.CASE_INSENSITIVE_ORDER)
        ));
  }
}
