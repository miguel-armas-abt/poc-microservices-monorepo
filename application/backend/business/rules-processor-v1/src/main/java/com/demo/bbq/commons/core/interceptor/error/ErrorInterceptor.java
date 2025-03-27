package com.demo.bbq.commons.core.interceptor.error;

import com.demo.bbq.commons.core.errors.dto.ErrorDTO;
import com.demo.bbq.commons.core.errors.exceptions.GenericException;
import com.demo.bbq.commons.core.logging.ThreadContextInjector;
import com.demo.bbq.commons.core.logging.enums.LoggingType;
import com.demo.bbq.commons.core.properties.ConfigurationBaseProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.ConnectException;

import static com.demo.bbq.commons.core.interceptor.error.ResponseErrorSelector.toErrorDTO;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ErrorInterceptor extends ResponseEntityExceptionHandler {

  private final ThreadContextInjector threadContextInjector;
  private final ConfigurationBaseProperties properties;

  @ExceptionHandler({Throwable.class})
  public ResponseEntity<ErrorDTO> handleException(Throwable ex, WebRequest request) {
    generateTrace(ex, request);

    ErrorDTO error = ErrorDTO.getDefaultError(properties);
    HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

    if (ex instanceof ResourceAccessException || ex instanceof ConnectException) {
      httpStatus = HttpStatus.REQUEST_TIMEOUT;
    }

    if (ex instanceof GenericException genericException) {
      error = toErrorDTO(properties, genericException);
      httpStatus = genericException.getHttpStatus();
    }

    return new ResponseEntity<>(error, httpStatus);
  }

  //jakarta validations
  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
                                                                HttpStatusCode status, WebRequest request) {
    generateTrace(ex, request);
    ErrorDTO error = toErrorDTO(properties, ex);
    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }

  private void generateTrace(Throwable ex, WebRequest request) {
    if (properties.isLoggerPresent(LoggingType.ERROR))
      threadContextInjector.populateFromException(ex, request);
  }
}