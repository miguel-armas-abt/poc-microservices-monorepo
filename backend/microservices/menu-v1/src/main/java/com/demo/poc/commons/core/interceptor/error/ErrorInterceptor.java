package com.demo.poc.commons.core.interceptor.error;

import com.demo.poc.commons.core.constants.Symbol;
import com.demo.poc.commons.core.errors.dto.ErrorDto;
import com.demo.poc.commons.core.errors.exceptions.GenericException;
import com.demo.poc.commons.core.errors.selector.ResponseErrorSelector;
import com.demo.poc.commons.core.logging.ErrorThreadContextInjector;
import com.demo.poc.commons.core.logging.enums.LoggingType;
import com.demo.poc.commons.custom.exceptions.ErrorDictionary;
import com.demo.poc.commons.custom.properties.ApplicationProperties;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

import java.util.stream.Collectors;

@ApplicationScoped
@RequiredArgsConstructor
public class ErrorInterceptor {

  private final ApplicationProperties properties;
  private final ErrorThreadContextInjector contextInjector;
  private final ResponseErrorSelector responseErrorSelector;

  @ServerExceptionMapper
  public RestResponse<ErrorDto> toResponse(Throwable throwable) {
    boolean isLoggerPresent = LoggingType.isLoggerPresent(properties, LoggingType.ERROR);
    if (isLoggerPresent) {
      contextInjector.populateFromException(throwable);
    }

    ErrorDto error = ErrorDto.getDefaultError(properties);
    Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;

    if (throwable instanceof WebApplicationException webApplicationException) {
      status = Response.Status.fromStatusCode(webApplicationException.getResponse().getStatus());
    }

    if (throwable instanceof ProcessingException) {
      status = Response.Status.REQUEST_TIMEOUT;
    }

    if (throwable instanceof GenericException genericException) {
      error = responseErrorSelector.toErrorDTO(genericException);
      status = genericException.getHttpStatus();
    }

    return RestResponse.status(status, error);
  }

  @ServerExceptionMapper
  public RestResponse<ErrorDto> toResponse(ConstraintViolationException exception) {
    contextInjector.populateFromException(exception);
    String message = exception
        .getConstraintViolations()
        .stream()
        .map(ConstraintViolation::getMessage)
        .collect(Collectors.joining(Symbol.COMMA_WITH_SPACE));

    ErrorDto error = ErrorDto.builder()
        .code(ErrorDictionary.INVALID_FIELD.getCode())
        .message(message)
        .build();

    Response.Status status = Response.Status.BAD_REQUEST;
    return RestResponse.status(status, error);
  }
}
