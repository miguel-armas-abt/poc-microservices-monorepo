package com.demo.poc.commons.core.restclient.utils;

import com.demo.poc.commons.core.properties.restclient.HeaderTemplate;
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

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HttpHeadersFiller {

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

  public static Consumer<HttpHeaders> fillHeaders(HeaderTemplate headerTemplate,
                                                  Map<String, String> currentHeaders) {
    return currentHttpHeaders -> currentHttpHeaders.setAll(HeadersFiller.fillHeaders(headerTemplate, currentHeaders));
  }

  public static Consumer<HttpHeaders> addAuthorizationHeader(String accessToken) {
    return httpHeaders -> httpHeaders.set(AUTHORIZATION, "Bearer ".concat(accessToken));
  }
}