package com.demo.bbq.business.menuoption.infrastructure.exception;

import com.demo.bbq.support.exception.catalog.ApiExceptionType;
import com.demo.bbq.support.exception.model.ApiException;
import com.demo.bbq.support.exception.model.ApiExceptionDetail;
import com.demo.bbq.support.exception.model.dto.ApiExceptionDto;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.context.support.DefaultMessageSourceResolvable;
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

    ApiExceptionDto apiException = ApiExceptionDto.builder()
        .type(ApiExceptionType.MALFORMED_REQUEST.getDescription())
        .message(ex.getCause().getMessage())
        .build();
    return new ResponseEntity<>(apiException, ApiExceptionType.MALFORMED_REQUEST.getHttpStatus());
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

    List<String> errorMessageList = ex.getBindingResult().getFieldErrors()
        .stream()
        .map(DefaultMessageSourceResolvable::getDefaultMessage)
        .collect(Collectors.toList());

    List<ApiExceptionDetail> detailList = new ArrayList<>();
    for (String errorMessage: errorMessageList) {
      detailList.add(ApiExceptionDetail.builder().message(errorMessage).build());
    }

    ApiExceptionDto exception = ApiExceptionDto.builder()
        .type(ApiExceptionType.MALFORMED_REQUEST.getDescription())
        .details(detailList)
        .build();

    return new ResponseEntity<>(exception, ApiExceptionType.MALFORMED_REQUEST.getHttpStatus());
  }

  @ExceptionHandler(ApiException.class)
  public final ResponseEntity<ApiExceptionDto> sendException(ApiException ex, WebRequest request) {
    ApiExceptionDto exception = ApiExceptionDto.builder()
        .type(ex.getType())
        .message(ex.getMessage())
        .errorCode(ex.getErrorCode())
        .details(ex.getDetails())
        .build();
    return new ResponseEntity<>(exception, ex.getHttpStatus());
  }

}
