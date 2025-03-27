package com.demo.poc.commons.core.interceptor.error;

import com.demo.poc.commons.core.errors.dto.ErrorDTO;
import com.demo.poc.commons.core.errors.exceptions.ExternalServiceException;
import com.demo.poc.commons.core.errors.exceptions.GenericException;
import com.demo.poc.commons.core.logging.ThreadContextInjector;
import com.demo.poc.commons.custom.properties.ApplicationProperties;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import lombok.RequiredArgsConstructor;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
@RequiredArgsConstructor
public class ErrorInterceptor implements ExceptionMapper<Throwable> {

  private final ApplicationProperties properties;
  private final ThreadContextInjector threadContextInjector;

  @Override
  public Response toResponse(Throwable throwable) {
    threadContextInjector.populateFromException(throwable);

    ErrorDTO error = ErrorDTO.getDefaultError(properties);
    Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;

    if (throwable instanceof WebApplicationException webApplicationException) {
      status = Response.Status.fromStatusCode(webApplicationException.getResponse().getStatus());
    }

    if (throwable instanceof ExternalServiceException externalServiceException) {
      error = externalServiceException.getErrorDetail();
      status = Response.Status.fromStatusCode(((ExternalServiceException) throwable).getHttpStatusCode().getStatusCode());
    }

    if (throwable instanceof GenericException genericException) {
      error = ResponseErrorSelector.toErrorDTO(properties, genericException);
      status = genericException.getHttpStatus();
    }

    return Response.status(status)
        .entity(error)
        .type(MediaType.APPLICATION_JSON)
        .build();
  }
}