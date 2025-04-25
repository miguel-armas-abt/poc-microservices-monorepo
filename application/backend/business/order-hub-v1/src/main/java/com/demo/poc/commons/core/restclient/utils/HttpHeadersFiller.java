package com.demo.poc.commons.core.restclient.utils;

import com.demo.poc.commons.core.properties.restclient.HeaderTemplate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;

import java.util.Map;
import java.util.function.Consumer;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HttpHeadersFiller {

  public static Consumer<HttpHeaders> fillHeaders(HeaderTemplate headerTemplate,
                                                  Map<String, String> currentHeaders) {
    return currentHttpHeaders -> currentHttpHeaders.setAll(HeadersFiller.fillHeaders(headerTemplate, currentHeaders));
  }

  public static Consumer<HttpHeaders> addAuthorizationHeader(String accessToken) {
    return httpHeaders -> httpHeaders.set(AUTHORIZATION, "Bearer ".concat(accessToken));
  }
}