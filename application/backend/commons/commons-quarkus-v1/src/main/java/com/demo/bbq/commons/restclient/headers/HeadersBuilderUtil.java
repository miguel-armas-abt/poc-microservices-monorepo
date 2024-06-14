package com.demo.bbq.commons.restclient.headers;

import com.demo.bbq.commons.properties.dto.HeaderTemplate;
import com.demo.bbq.commons.properties.enums.GeneratedHeaderType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HeadersBuilderUtil {

  public static MultivaluedMap<String, String> buildHeaders(HeaderTemplate headerTemplate,
                                                            MultivaluedMap<String, String> requestHeaders) {

      Consumer<Map<String, String>> providedHeaders = buildProvidedHeaders(headerTemplate.provided());
      Consumer<Map<String, String>> generatedHeaders = buildGenerateHeaders(headerTemplate.generated());
      Consumer<Map<String, String>> forwardedHeaders = buildForwardedHeaders(headerTemplate.forwarded(), requestHeaders);

      Map<String, String> headers = new HashMap<>();
      providedHeaders.accept(headers);
      generatedHeaders.accept(headers);
      forwardedHeaders.accept(headers);

      MultivaluedMap<String, String> headersMultivaluedMap = new MultivaluedHashMap<>();
      headers.forEach(headersMultivaluedMap::add);
      return headersMultivaluedMap;
  }

  private static Consumer<Map<String, String>> buildProvidedHeaders(Map<String, String> providedHeaders) {
    return headers -> Optional.ofNullable(providedHeaders).ifPresent(headers::putAll);
  }

  private static Consumer<Map<String, String>> buildGenerateHeaders(Map<String, String> generatedHeaders) {
    return headers -> Optional.ofNullable(generatedHeaders).ifPresent(headersMap -> headersMap
        .forEach((headerKey, generatedHeaderType) -> headers.put(headerKey, GeneratedHeaderType.valueOf(generatedHeaderType).getHeaderGenerator().generateHeader())));
  }

  private static Consumer<Map<String, String>> buildForwardedHeaders(Map<String, String> forwardedHeaders,
                                                                     MultivaluedMap<String, String> requestHeaders) {

    return headers -> Optional.ofNullable(forwardedHeaders).ifPresent(headerMap -> headerMap
        .forEach((headerKey, headerName) -> setForwardedHeaderIfPresent(headers, headerKey, headerName, requestHeaders)));
  }

  private static void setForwardedHeaderIfPresent(Map<String, String> forwardedHeaders,
                                                  String headerKey,
                                                  String headerName,
                                                  MultivaluedMap<String, String> requestHeaders) {

    Optional<String> currentHeader = Optional.ofNullable(requestHeaders.getFirst(headerName));
    currentHeader.ifPresent(headerValue -> forwardedHeaders.put(headerKey, headerValue));
  }
}