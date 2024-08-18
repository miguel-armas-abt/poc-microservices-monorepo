package com.demo.bbq.commons.toolkit.params.filler;

import com.demo.bbq.commons.toolkit.params.enums.GeneratedParamType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HeadersFillerBase {

  public static Consumer<Map<String, String>> addProvidedHeaders(Map<String, String> providedHeaders) {
    return headers -> Optional.ofNullable(providedHeaders).ifPresent(headers::putAll);
  }

  public static Consumer<Map<String, String>> addGenerateHeaders(Map<String, GeneratedParamType> generatedHeaders) {
    return headers -> Optional.ofNullable(generatedHeaders).ifPresent(headersMap -> headersMap
        .forEach((headerKey, generatedParamType) -> headers.put(headerKey, generatedParamType.getParamGenerator().generateHeader())));
  }

  public static Consumer<Map<String, String>> addForwardedHeaders(Map<String, String> forwardedHeaders,
                                                                  Map<String, String> currentHeaders) {
    return headers -> Optional.ofNullable(forwardedHeaders).ifPresent(headerMap -> headerMap
        .forEach((headerKey, headerName) -> setForwardedHeaderIfPresent(headers, currentHeaders, headerKey, headerName)));
  }

  private static void setForwardedHeaderIfPresent(Map<String, String> forwardedHeaders,
                                                  Map<String, String> currentHeaders,
                                                  String headerKey,
                                                  String headerName) {
    Optional<String> currentHeader = Optional.ofNullable(currentHeaders.get(headerName));
    currentHeader.ifPresent(headerValue -> forwardedHeaders.put(headerKey, headerValue));
  }
}
