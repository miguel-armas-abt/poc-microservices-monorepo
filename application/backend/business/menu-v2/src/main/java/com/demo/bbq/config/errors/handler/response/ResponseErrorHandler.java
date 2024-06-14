package com.demo.bbq.config.errors.handler.response;

import com.demo.bbq.config.properties.ServiceConfigurationProperties;
import com.demo.bbq.commons.errors.handler.response.ResponseErrorHandlerUtil;
import lombok.RequiredArgsConstructor;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
@RequiredArgsConstructor
public class ResponseErrorHandler implements ExceptionMapper<Throwable> {

  private final ServiceConfigurationProperties properties;

  @Override
  public Response toResponse(Throwable throwable) {
    return ResponseErrorHandlerUtil.toResponse(throwable, properties);
  }
}