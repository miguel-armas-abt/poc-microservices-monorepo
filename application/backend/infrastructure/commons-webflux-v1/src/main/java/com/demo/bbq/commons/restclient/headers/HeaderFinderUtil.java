package com.demo.bbq.commons.restclient.headers;

import com.demo.bbq.commons.errors.exceptions.SystemException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.reactive.function.server.ServerRequest;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HeaderFinderUtil {

  public static String findHeader(Object request, String headerName) {
    if (request instanceof ServerRequest serverRequest)
      return serverRequest.headers().firstHeader(headerName);

    if (request instanceof ServerHttpRequest serverHttpRequest)
      return serverHttpRequest.getHeaders().getFirst(headerName);

    throw new SystemException("InvalidServerRequest");
  }
}
