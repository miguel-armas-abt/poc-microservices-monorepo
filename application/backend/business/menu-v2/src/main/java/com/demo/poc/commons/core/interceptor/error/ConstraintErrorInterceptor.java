package com.demo.poc.commons.core.interceptor.error;

import com.demo.poc.commons.core.errors.dto.ErrorDto;
import com.demo.poc.commons.core.logging.ThreadContextInjector;
import com.demo.poc.commons.custom.properties.ApplicationProperties;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ConstraintErrorInterceptor implements ExceptionMapper<ConstraintViolationException> {

  private final ApplicationProperties properties;
  private final ThreadContextInjector threadContextInjector;

  @Override
  public Response toResponse(ConstraintViolationException exception) {
    threadContextInjector.populateFromException(exception);

    ErrorDto error = ResponseErrorSelector.toErrorDTO(properties, exception);
    Response.Status status = Response.Status.BAD_REQUEST;

    return Response.status(status)
        .entity(error)
        .type(MediaType.APPLICATION_JSON)
        .build();
  }
}
