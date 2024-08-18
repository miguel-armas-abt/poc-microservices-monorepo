package com.demo.bbq.commons.toolkit.params.filler;

import static com.demo.bbq.commons.toolkit.params.filler.HeadersFillerBase.addForwardedHeaders;
import static com.demo.bbq.commons.toolkit.params.filler.HeadersFillerBase.addGenerateHeaders;
import static com.demo.bbq.commons.toolkit.params.filler.HeadersFillerBase.addProvidedHeaders;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import com.demo.bbq.commons.properties.dto.restclient.HeaderTemplate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.reactive.function.server.ServerRequest;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HeadersFiller {

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

  public static Consumer<HttpHeaders> buildHeaders(HeaderTemplate headerTemplate,
                                                   Map<String, String> currentHeaders) {
    return currentHttpHeaders -> {
      Consumer<Map<String, String>> providedHeaders = addProvidedHeaders(headerTemplate.getProvided());
      Consumer<Map<String, String>> generatedHeaders = addGenerateHeaders(headerTemplate.getGenerated());
      Consumer<Map<String, String>> forwardedHeaders = addForwardedHeaders(headerTemplate.getForwarded(), currentHeaders);

      Map<String, String> headers = new HashMap<>();
      providedHeaders.accept(headers);
      generatedHeaders.accept(headers);
      forwardedHeaders.accept(headers);

      currentHttpHeaders.setAll(headers);
    };
  }

  public static Consumer<HttpHeaders> buildAuthorizationHeader(String accessToken) {
    return httpHeaders -> httpHeaders.set(AUTHORIZATION, "Bearer ".concat(accessToken));
  }
}