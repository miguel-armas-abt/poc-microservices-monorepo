package com.demo.bbq.application.utils;

import com.demo.bbq.utils.errors.exceptions.AuthorizationException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.server.ServerWebExchange;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TokenValidatorUtil {

  public static void validateAuthToken(ServerWebExchange serverWebExchange) {
    HttpHeaders httpHeaders = serverWebExchange.getRequest().getHeaders();

    if (!httpHeaders.containsKey(HttpHeaders.AUTHORIZATION))
      throw new AuthorizationException("MissingAuthorizationHeader", "Missing Authorization header");

    String authorizationHeader = httpHeaders.getFirst(HttpHeaders.AUTHORIZATION);
    String[] authElements = authorizationHeader.split(" ");
    if (authElements.length != 2 || !"Bearer".equals(authElements[0]))
      throw new AuthorizationException("InvalidAuthorizationStructure", "Invalid authorization structure");
  }
}
