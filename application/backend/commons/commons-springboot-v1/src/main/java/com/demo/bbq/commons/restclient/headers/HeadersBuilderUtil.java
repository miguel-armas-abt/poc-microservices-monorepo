package com.demo.bbq.commons.restclient.headers;

import com.demo.bbq.commons.properties.dto.restclient.HeaderTemplate;
import com.demo.bbq.commons.toolkit.restclient.headers.GeneratedHeaderType;
import jakarta.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HeadersBuilderUtil {

  public static Map<String, String> parseHeaders(HttpServletRequest httpServletRequest) {
    return Optional.ofNullable(httpServletRequest.getHeaderNames())
        .map(Collections::list)
        .orElse(new ArrayList<>())
        .stream()
        .collect(Collectors.toMap(headerName -> headerName, httpServletRequest::getHeader));
  }

  public static HttpHeaders buildHeaders(Map<String, String> currentHeaders, HeaderTemplate headerTemplate) {
    Consumer<Map<String, String>> providedHeaders = buildProvidedHeaders(headerTemplate.getProvided());
    Consumer<Map<String, String>> generatedHeaders = buildGenerateHeaders(headerTemplate.getGenerated());
    Consumer<Map<String, String>> forwardedHeaders = buildForwardedHeaders(headerTemplate.getForwarded(), currentHeaders);

    Map<String, String> headers = new HashMap<>();
    providedHeaders.accept(headers);
    generatedHeaders.accept(headers);
    forwardedHeaders.accept(headers);

    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setAll(headers);
    return httpHeaders;
  }

  private static Consumer<Map<String, String>> buildProvidedHeaders(Map<String, String> providedHeaders) {
    return headers -> Optional.ofNullable(providedHeaders).ifPresent(headers::putAll);
  }

  private static Consumer<Map<String, String>> buildGenerateHeaders(Map<String, GeneratedHeaderType> generatedHeaders) {
    return headers -> Optional.ofNullable(generatedHeaders).ifPresent(headersMap -> headersMap
        .forEach((headerKey, generatedHeaderType) -> headers.put(headerKey, generatedHeaderType.getHeaderGenerator().generateHeader())));
  }

  private static Consumer<Map<String, String>> buildForwardedHeaders(Map<String, String> forwardedHeaders,
                                                                     Map<String, String> currentHeaders) {
    return headers -> Optional.ofNullable(forwardedHeaders).ifPresent(headerMap -> headerMap
        .forEach((headerKey, headerName) -> setForwardedHeaderIfPresent(currentHeaders, headers, headerKey, headerName)));
  }

  private static void setForwardedHeaderIfPresent(Map<String, String> currentHeaders,
                                                  Map<String, String> forwardedHeaders,
                                                  String headerKey,
                                                  String headerName) {
    Optional<String> currentHeader = Optional.ofNullable(currentHeaders.get(headerName));
    currentHeader.ifPresent(headerValue -> forwardedHeaders.put(headerKey, headerValue));
  }

}
