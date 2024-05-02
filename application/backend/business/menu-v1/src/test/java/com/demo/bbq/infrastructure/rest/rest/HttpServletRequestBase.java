package com.demo.bbq.infrastructure.rest.rest;

import jakarta.servlet.http.HttpServletRequest;
import org.mockito.Mockito;

public class HttpServletRequestBase {

  public static HttpServletRequest buildHttpServletRequest() {
    HttpServletRequest servletRequest = Mockito.mock(HttpServletRequest.class);
    return servletRequest;
  }

}
