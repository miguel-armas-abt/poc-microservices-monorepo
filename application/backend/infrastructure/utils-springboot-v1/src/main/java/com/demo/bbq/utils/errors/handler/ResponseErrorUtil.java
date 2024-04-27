package com.demo.bbq.utils.errors.handler;

import com.demo.bbq.utils.errors.dto.ErrorDTO;
import com.demo.bbq.utils.errors.exceptions.AuthorizationException;
import com.demo.bbq.utils.errors.exceptions.BusinessException;
import com.demo.bbq.utils.errors.exceptions.ExternalServiceException;
import com.demo.bbq.utils.errors.exceptions.SystemException;
import com.demo.bbq.utils.errors.matcher.ErrorMatcherUtil;
import com.demo.bbq.utils.properties.ConfigurationBaseProperties;
import java.util.Objects;
import java.util.function.BiFunction;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.context.request.WebRequest;

public class ResponseErrorUtil {

  private ResponseErrorUtil() {}

  public static ResponseEntity<ErrorDTO> handleException(ConfigurationBaseProperties properties, Throwable ex, WebRequest request) {
    ErrorDTO error = ErrorMatcherUtil.getDefaultError(properties);
    HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

    if(ex instanceof ResourceAccessException) {
      httpStatus = HttpStatus.REQUEST_TIMEOUT;
    }

    if(ex instanceof ExternalServiceException externalServiceException) {
      error = externalServiceException.getErrorDetail();
      httpStatus = HttpStatus.valueOf(externalServiceException.getHttpStatusCode().value());
    }

    if(ex instanceof BusinessException businessException) {
      error = ErrorMatcherUtil.build(properties, businessException);
      httpStatus = HttpStatus.BAD_REQUEST;
    }

    if(ex instanceof SystemException systemException) {
      error = ErrorMatcherUtil.build(properties, systemException);
      httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    if(ex instanceof AuthorizationException authException) {
      error = ErrorMatcherUtil.build(properties, authException);
      httpStatus = HttpStatus.UNAUTHORIZED;
    }

    return new ResponseEntity<>(error, httpStatus);
  }

  public static BiFunction<ConfigurationBaseProperties, HttpMessageNotReadableException, ResponseEntity<Object>> handleHttpMessageNotReadable = (properties, exception) -> {
    ErrorDTO error = ErrorMatcherUtil.getDefaultError(properties);
    error.setMessage(exception.getCause().getMessage());
    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  };

  public static BiFunction<ConfigurationBaseProperties, MethodArgumentNotValidException, ResponseEntity<Object>> handleMethodArgumentNotValid = (properties, exception) -> {
    ErrorDTO error = ErrorMatcherUtil.getDefaultError(properties);

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
