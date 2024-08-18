package com.demo.bbq.commons.toolkit.params.filler;

import java.util.Map;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.web.reactive.function.server.ServerRequest;

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
