package com.demo.poc.commons.core.restclient.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.Map;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QueryParamFiller {

  public static Map<String, String> extractQueryParamsAsMap(ServerRequest serverRequest) {
    return serverRequest.queryParams()
        .entrySet()
        .stream()
        .collect(Collectors.toMap(
            Map.Entry::getKey,
            entry -> String.join(",", entry.getValue())
        ));
  }
}
