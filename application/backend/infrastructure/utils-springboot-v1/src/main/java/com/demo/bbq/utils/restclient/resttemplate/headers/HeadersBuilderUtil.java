package com.demo.bbq.utils.restclient.resttemplate.headers;

import com.demo.bbq.utils.properties.dto.GeneratedHeaderType;
import com.demo.bbq.utils.properties.dto.HeaderTemplate;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HeadersBuilderUtil {

  public static HttpHeaders buildHeaders(HttpServletRequest httpServletRequest, HeaderTemplate headerTemplate) {
    Consumer<Map<String, String>> providedHeaders = buildProvidedHeaders(headerTemplate.getProvided());
    Consumer<Map<String, String>> generatedHeaders = buildGenerateHeaders(headerTemplate.getGenerated());
    Consumer<Map<String, String>> forwardedHeaders = buildForwardedHeaders(headerTemplate.getForwarded(), httpServletRequest);

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
                                                                     HttpServletRequest httpServletRequest) {
    return headers -> Optional.ofNullable(forwardedHeaders).ifPresent(headerMap -> headerMap
        .forEach((headerKey, headerName) -> setForwardedHeaderIfPresent(httpServletRequest, headers, headerKey, headerName)));
  }

  private static void setForwardedHeaderIfPresent(HttpServletRequest httpServletRequest,
                                                  Map<String, String> forwardedHeaders,
                                                  String headerKey,
                                                  String headerName) {
    Optional<String> currentHeader = Optional.ofNullable(httpServletRequest.getHeader(headerName));
    currentHeader.ifPresent(headerValue -> forwardedHeaders.put(headerKey, headerValue));
  }

}
