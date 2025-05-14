package com.demo.poc.commons.core.restclient.utils;

import jakarta.ws.rs.core.MultivaluedMap;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HeadersExtractor {

  public static <T, Q> Map<String, String> extractHeadersAsMap(MultivaluedMap<T, Q> headers) {
    return Optional.ofNullable(headers)
        .map(Map::entrySet)
        .map(entrySet ->
            entrySet
            .stream()
            .filter(element -> Objects.nonNull(element) && Objects.nonNull(element.getValue()) && !element.getValue().isEmpty())
            .collect(Collectors.toMap(
                element -> element.getKey().toString(),
                element -> {
                  Q value = element.getValue().get(0);
                  return Objects.nonNull(value) ? value.toString() : StringUtils.EMPTY;
                })
            ))
        .orElse(Collections.emptyMap());
  }
}
