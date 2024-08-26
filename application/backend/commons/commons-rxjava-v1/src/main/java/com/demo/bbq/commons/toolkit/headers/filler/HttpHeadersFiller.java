package com.demo.bbq.commons.toolkit.headers.filler;

import com.demo.bbq.commons.properties.dto.restclient.HeaderTemplate;
import com.demo.bbq.commons.toolkit.params.filler.HeadersFiller;
import jakarta.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;

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