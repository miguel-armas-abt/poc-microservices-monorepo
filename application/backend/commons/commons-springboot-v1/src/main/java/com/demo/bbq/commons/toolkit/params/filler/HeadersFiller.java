package com.demo.bbq.commons.toolkit.params.filler;

import static com.demo.bbq.commons.toolkit.params.filler.HeadersFillerBase.addForwardedHeaders;
import static com.demo.bbq.commons.toolkit.params.filler.HeadersFillerBase.addGenerateHeaders;
import static com.demo.bbq.commons.toolkit.params.filler.HeadersFillerBase.addProvidedHeaders;

import com.demo.bbq.commons.properties.dto.restclient.HeaderTemplate;
import jakarta.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HeadersFiller {

  public static Map<String, String> extractHeadersAsMap(HttpServletRequest httpServletRequest) {
    return Optional.ofNullable(httpServletRequest.getHeaderNames())
        .map(Collections::list)
        .orElse(new ArrayList<>())
        .stream()
        .collect(Collectors.toMap(headerName -> headerName, httpServletRequest::getHeader));
  }

  public static HttpHeaders buildHeaders(HeaderTemplate headerTemplate, Map<String, String> currentHeaders) {
    Consumer<Map<String, String>> providedHeaders = addProvidedHeaders(headerTemplate.getProvided());
    Consumer<Map<String, String>> generatedHeaders = addGenerateHeaders(headerTemplate.getGenerated());
    Consumer<Map<String, String>> forwardedHeaders = addForwardedHeaders(headerTemplate.getForwarded(), currentHeaders);

    Map<String, String> headers = new HashMap<>();
    providedHeaders.accept(headers);
    generatedHeaders.accept(headers);
    forwardedHeaders.accept(headers);

    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setAll(headers);
    return httpHeaders;
  }
}