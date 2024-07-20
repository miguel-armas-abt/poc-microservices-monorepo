package com.demo.bbq.config.errors.handler.response;

import com.demo.bbq.commons.errors.handler.response.ResponseErrorHandlerUtil;
import com.demo.bbq.config.properties.ApplicationProperties;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.RequiredArgsConstructor;

@Provider
@RequiredArgsConstructor
public class ConstraintErrorHandler implements ExceptionMapper<ConstraintViolationException> {

  private final ApplicationProperties properties;

  @Override
  public Response toResponse(ConstraintViolationException exception) {
    return ResponseErrorHandlerUtil.toResponse(exception, properties);
  }
}
