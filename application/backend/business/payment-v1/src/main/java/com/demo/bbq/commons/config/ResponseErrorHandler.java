package com.demo.bbq.commons.config;

import com.demo.bbq.commons.properties.ApplicationProperties;
import com.demo.bbq.commons.errors.dto.ErrorDTO;
import com.demo.bbq.commons.errors.handler.external.strategy.RestClientErrorStrategy;
import com.demo.bbq.commons.errors.handler.response.ResponseErrorHandlerUtil;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@RequiredArgsConstructor
public class ResponseErrorHandler extends ResponseEntityExceptionHandler {

  private final ApplicationProperties properties;
  private final  List<RestClientErrorStrategy> errorServices;

  @ExceptionHandler({Throwable.class})
  public final ResponseEntity<ErrorDTO> handleException(Throwable exception, WebRequest request) {
    return ResponseErrorHandlerUtil.handleException(properties, errorServices, exception, request);
  }
}