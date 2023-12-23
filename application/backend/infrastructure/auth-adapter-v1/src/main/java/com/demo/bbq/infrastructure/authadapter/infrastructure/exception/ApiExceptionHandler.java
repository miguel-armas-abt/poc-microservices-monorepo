package com.demo.bbq.infrastructure.authadapter.infrastructure.exception;

import com.demo.bbq.support.exception.model.ApiException;
import com.demo.bbq.support.exception.model.dto.ApiExceptionDto;
import com.demo.bbq.support.exception.util.ApiExceptionHandlerUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(
      HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
    return ApiExceptionHandlerUtil.handleHttpMessageNotReadable.apply(ex);
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
    return ApiExceptionHandlerUtil.handleMethodArgumentNotValid.apply(ex);
  }

  @ExceptionHandler(ApiException.class)
  public final ResponseEntity<ApiExceptionDto> handleApiException(ApiException ex, WebRequest request) {
    return ApiExceptionHandlerUtil.handleApiException.apply(ex);
  }

}
