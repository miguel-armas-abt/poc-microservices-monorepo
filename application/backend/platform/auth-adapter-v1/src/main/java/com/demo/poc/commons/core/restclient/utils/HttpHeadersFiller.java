package com.demo.poc.commons.core.restclient.utils;

import com.demo.poc.commons.core.properties.restclient.HeaderTemplate;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HttpHeadersFiller {

  public static Map<String, String> extractHeadersAsMap(HttpServletRequest httpServletRequest) {
    return Optional.ofNullable(httpServletRequest.getHeaderNames())
        .map(Collections::list)
        .orElse(new ArrayList<>())
        .stream()
        .collect(Collectors.toMap(headerName -> headerName, httpServletRequest::getHeader));
  }

  public static HttpHeaders fillHeaders(HeaderTemplate headerTemplate, Map<String, String> currentHeaders) {
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setAll(HeadersFiller.fillHeaders(headerTemplate, currentHeaders));
    return httpHeaders;
  }
}