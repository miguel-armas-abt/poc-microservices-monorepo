package com.demo.poc.commons.core.restserver.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RestServerUtils {

  public static Map<String, String> extractHeadersAsMap(HttpServletRequest httpServletRequest) {
    return Optional.ofNullable(httpServletRequest.getHeaderNames())
        .map(Collections::list)
        .orElse(new ArrayList<>())
        .stream()
        .collect(Collectors.toMap(
            headerName -> headerName,
            httpServletRequest::getHeader,
            (u, v) -> v,
            () -> new TreeMap<>(String.CASE_INSENSITIVE_ORDER)
        ));
  }

  public static Map<String, String> extractQueryParamsAsMap(HttpServletRequest request) {
    return Optional.ofNullable(request.getParameterNames())
        .map(Collections::list)
        .orElse(new ArrayList<>())
        .stream()
        .collect(Collectors.toMap(
            paramName -> paramName,
            request::getParameter
        ));
  }
}
