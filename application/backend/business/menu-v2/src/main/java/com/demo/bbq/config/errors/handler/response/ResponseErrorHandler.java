package com.demo.bbq.config.errors.handler.response;

import com.demo.bbq.config.properties.ApplicationProperties;
import com.demo.bbq.commons.errors.handler.response.ResponseErrorHandlerUtil;
import lombok.RequiredArgsConstructor;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
@RequiredArgsConstructor
public class ResponseErrorHandler implements ExceptionMapper<Throwable> {

  private final ApplicationProperties properties;

  @Override
  public Response toResponse(Throwable throwable) {
    return ResponseErrorHandlerUtil.toResponse(throwable, properties);
  }
}