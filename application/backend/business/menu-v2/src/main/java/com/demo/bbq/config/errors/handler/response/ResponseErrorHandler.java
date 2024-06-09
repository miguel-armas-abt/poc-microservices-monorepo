package com.demo.bbq.config.errors.handler.response;

import com.demo.bbq.config.utils.errors.dto.ErrorDTO;
import com.demo.bbq.config.utils.errors.exceptions.AuthorizationException;
import com.demo.bbq.config.utils.errors.exceptions.BusinessException;
import com.demo.bbq.config.utils.errors.exceptions.ExternalServiceException;
import com.demo.bbq.config.utils.errors.exceptions.SystemException;
import com.demo.bbq.config.properties.ConfigurationBaseProperties;
import com.demo.bbq.config.tracing.logging.ErrorLoggingUtil;
import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ResponseErrorHandler implements ExceptionMapper<Throwable> {

  @Inject
  ConfigurationBaseProperties properties;

  @Override
  public Response toResponse(Throwable throwable) {
    ErrorLoggingUtil.generateTrace(throwable);

    ErrorDTO error = ErrorDTO.getDefaultError(properties);
    Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;

    if (throwable instanceof WebApplicationException webApplicationException) {
      status = Response.Status.fromStatusCode(webApplicationException.getResponse().getStatus());
    }

    if (throwable instanceof ExternalServiceException externalServiceException) {
      error = externalServiceException.getErrorDetail();
      status = Response.Status.fromStatusCode(((ExternalServiceException) throwable).getHttpStatusCode().getStatusCode());
    }

    if (throwable instanceof BusinessException businessException) {
      error = ResponseErrorHandlerBaseUtil.build(properties, businessException);
      status = Response.Status.BAD_REQUEST;
    }

    if (throwable instanceof SystemException systemException) {
      error = ResponseErrorHandlerBaseUtil.build(properties, systemException);
    }

    if (throwable instanceof AuthorizationException authorizationException) {
      error = ResponseErrorHandlerBaseUtil.build(properties, authorizationException);
      status = Response.Status.UNAUTHORIZED;
    }

    return Response.status(status)
        .entity(error)
        .type(MediaType.APPLICATION_JSON)
        .build();
  }
}
