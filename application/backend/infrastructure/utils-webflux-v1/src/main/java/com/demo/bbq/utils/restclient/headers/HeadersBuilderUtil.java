package com.demo.bbq.utils.restclient.headers;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import com.demo.bbq.utils.properties.dto.GeneratedHeaderType;
import com.demo.bbq.utils.properties.dto.HeaderTemplate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HeadersBuilderUtil {

  public static Consumer<HttpHeaders> buildHeaders(HeaderTemplate headerTemplate,
                                                   Object request) {
    return currentHttpHeaders -> {
      Consumer<Map<String, String>> providedHeaders = buildProvidedHeaders(headerTemplate.getProvided());
      Consumer<Map<String, String>> generatedHeaders = buildGenerateHeaders(headerTemplate.getGenerated());
      Consumer<Map<String, String>> forwardedHeaders = buildForwardedHeaders(headerTemplate.getForwarded(), request);

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

  private static Consumer<Map<String, String>> buildProvidedHeaders(Map<String, String> providedHeaders) {
    return headers -> Optional.ofNullable(providedHeaders).ifPresent(headers::putAll);
  }

  private static Consumer<Map<String, String>> buildGenerateHeaders(Map<String, GeneratedHeaderType> generatedHeaders) {
    return headers -> Optional.ofNullable(generatedHeaders).ifPresent(headersMap -> headersMap
        .forEach((headerKey, generatedHeaderType) -> headers.put(headerKey, generatedHeaderType.getHeaderGenerator().generateHeader())));
  }

  private static Consumer<Map<String, String>> buildForwardedHeaders(Map<String, String> forwardedHeaders,
                                                                     Object request) {

    return headers -> Optional.ofNullable(forwardedHeaders).ifPresent(headerMap -> headerMap
        .forEach((headerKey, headerName) -> setForwardedHeaderIfPresent(headers, headerKey, headerName, request)));
  }

  private static void setForwardedHeaderIfPresent(Map<String, String> forwardedHeaders,
                                                  String headerKey,
                                                  String headerName,
                                                  Object request) {

    Optional<String> currentHeader = Optional.ofNullable(HeaderFinderUtil.findHeader(request, headerName));
    currentHeader.ifPresent(headerValue -> forwardedHeaders.put(headerKey, headerValue));
  }
}