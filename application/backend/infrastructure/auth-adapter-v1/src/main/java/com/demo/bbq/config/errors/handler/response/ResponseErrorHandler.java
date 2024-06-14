package com.demo.bbq.config.errors.handler.response;

import java.util.List;
import com.demo.bbq.application.properties.ServiceConfigurationProperties;
import com.demo.bbq.commons.errors.dto.ErrorDTO;
import com.demo.bbq.commons.errors.handler.external.strategy.RestClientErrorStrategy;
import com.demo.bbq.commons.errors.handler.response.ResponseErrorHandlerUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@RequiredArgsConstructor
public class ResponseErrorHandler extends ResponseEntityExceptionHandler {

  private final ServiceConfigurationProperties properties;
  private final  List<RestClientErrorStrategy> strategies;

  @ExceptionHandler({Throwable.class})
  public final ResponseEntity<ErrorDTO> handleException(Throwable exception, WebRequest request) {
    return ResponseErrorHandlerUtil.handleException(properties, strategies, exception, request);
  }
}