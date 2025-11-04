package com.demo.service.entrypoint.auth.utils;

import com.demo.service.commons.exceptions.InvalidAuthorizationStructureException;
import com.demo.service.commons.exceptions.MissingAuthorizationHeaderException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.server.ServerWebExchange;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TokenValidatorUtil {

  public static void validateAuthToken(ServerWebExchange serverWebExchange) {
    HttpHeaders httpHeaders = serverWebExchange.getRequest().getHeaders();

    if (!httpHeaders.containsKey(HttpHeaders.AUTHORIZATION))
      throw new MissingAuthorizationHeaderException();

    String authorizationHeader = httpHeaders.getFirst(HttpHeaders.AUTHORIZATION);
    String[] authElements = authorizationHeader.split(" ");
    if (authElements.length != 2 || !"Bearer".equals(authElements[0]))
      throw new InvalidAuthorizationStructureException();
  }
}
