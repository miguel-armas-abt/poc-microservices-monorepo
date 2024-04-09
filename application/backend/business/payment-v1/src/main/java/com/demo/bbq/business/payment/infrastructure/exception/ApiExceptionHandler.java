package com.demo.bbq.business.payment.infrastructure.exception;

import com.demo.bbq.support.exception.model.ApiException;
import com.demo.bbq.support.exception.model.dto.ApiExceptionDTO;
import com.demo.bbq.support.exception.util.ApiExceptionHandlerUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(ApiException.class)
  public final ResponseEntity<ApiExceptionDTO> handleApiException(ApiException ex, WebRequest request) {
    return ApiExceptionHandlerUtil.handleApiException.apply(ex);
  }

}
