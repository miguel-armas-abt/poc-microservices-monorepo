package com.demo.bbq.business.orderhub.infrastructure.config.errors.handler;

import com.demo.bbq.business.orderhub.application.properties.ServiceConfigurationProperties;
import com.demo.bbq.utils.errors.dto.ErrorDTO;
import com.demo.bbq.utils.errors.external.RestClientErrorService;
import com.demo.bbq.utils.errors.handler.ResponseErrorUtil;
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

  private final ServiceConfigurationProperties properties;
  private final  List<RestClientErrorService> errorServices;

  @ExceptionHandler({Throwable.class})
  public final ResponseEntity<ErrorDTO> handleException(Throwable exception, WebRequest request) {
    return ResponseErrorUtil.handleException(properties, errorServices, exception, request);
  }
}