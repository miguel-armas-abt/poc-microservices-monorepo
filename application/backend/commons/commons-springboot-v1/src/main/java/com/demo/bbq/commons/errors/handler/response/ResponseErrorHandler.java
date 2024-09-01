package com.demo.bbq.commons.errors.handler.response;

import com.demo.bbq.commons.errors.dto.ErrorDTO;
import com.demo.bbq.commons.errors.exceptions.AuthorizationException;
import com.demo.bbq.commons.errors.exceptions.BusinessException;
import com.demo.bbq.commons.errors.exceptions.ExternalServiceException;
import com.demo.bbq.commons.errors.exceptions.SystemException;
import com.demo.bbq.commons.properties.ConfigurationBaseProperties;
import java.util.Objects;
import java.util.function.BiFunction;
import com.demo.bbq.commons.tracing.logging.error.ErrorLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@RequiredArgsConstructor
public class ResponseErrorHandler extends ResponseEntityExceptionHandler {

  private final ErrorLogger errorLogger;
  private final ConfigurationBaseProperties properties;

  @ExceptionHandler({Throwable.class})
  public ResponseEntity<ErrorDTO> handleException(Throwable ex, WebRequest request) {
    errorLogger.generateTraceIfLoggerIsPresent(ex, request);

    ErrorDTO error = ErrorDTO.getDefaultError(properties);
    HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

    if(ex instanceof ResourceAccessException) {
      httpStatus = HttpStatus.REQUEST_TIMEOUT;
    }

    if(ex instanceof ExternalServiceException externalServiceException) {
      error = externalServiceException.getErrorDetail();
      httpStatus = HttpStatus.valueOf(externalServiceException.getHttpStatusCode().value());
    }

    if(ex instanceof BusinessException businessException) {
      error = ResponseErrorHandlerBase.toErrorDTO(properties, businessException);
      httpStatus = HttpStatus.BAD_REQUEST;
    }

    if(ex instanceof SystemException systemException) {
      error = ResponseErrorHandlerBase.toErrorDTO(properties, systemException);
    }

    if(ex instanceof AuthorizationException authException) {
      error = ResponseErrorHandlerBase.toErrorDTO(properties, authException);
      httpStatus = HttpStatus.UNAUTHORIZED;
    }

    return new ResponseEntity<>(error, httpStatus);
  }

  public static BiFunction<ConfigurationBaseProperties, HttpMessageNotReadableException, ResponseEntity<Object>> handleHttpMessageNotReadable = (properties, exception) -> {
    ErrorDTO error = ErrorDTO.getDefaultError(properties);
    error.setMessage(exception.getCause().getMessage());
    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  };

  public static BiFunction<ConfigurationBaseProperties, MethodArgumentNotValidException, ResponseEntity<Object>> handleMethodArgumentNotValid = (properties, exception) -> {
    ErrorDTO error = ErrorDTO.getDefaultError(properties);

    String message = exception.getBindingResult().getFieldErrors()
        .stream()
        .map(DefaultMessageSourceResolvable::getDefaultMessage)
        .filter(Objects::nonNull)
        .reduce(String::concat)
        .orElseGet(error::getMessage);

    error.setMessage(message);
    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  };
}
