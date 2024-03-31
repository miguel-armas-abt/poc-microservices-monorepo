package com.demo.bbq.support.exception.util;

import com.demo.bbq.support.exception.enums.ApiExceptionType;
import com.demo.bbq.support.exception.model.ApiException;
import com.demo.bbq.support.exception.model.ApiExceptionDetail;
import com.demo.bbq.support.exception.model.dto.ApiExceptionDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;

public class ApiExceptionHandlerUtil {
  private ApiExceptionHandlerUtil() {}

  public static Function<HttpMessageNotReadableException, ResponseEntity<Object>> handleHttpMessageNotReadable = exception -> {
    ApiExceptionDTO apiException = ApiExceptionDTO.builder()
        .type(ApiExceptionType.MALFORMED_REQUEST.getDescription())
        .message(exception.getCause().getMessage())
        .build();
    return new ResponseEntity<>(apiException, ApiExceptionType.MALFORMED_REQUEST.getHttpStatus());
  };

  public static Function<MethodArgumentNotValidException, ResponseEntity<Object>> handleMethodArgumentNotValid = exception -> {
    List<String> errorMessageList = exception.getBindingResult().getFieldErrors()
        .stream()
        .map(DefaultMessageSourceResolvable::getDefaultMessage)
        .collect(Collectors.toList());

    List<ApiExceptionDetail> detailList = new ArrayList<>();
    for (String errorMessage: errorMessageList) {
      detailList.add(ApiExceptionDetail.builder().message(errorMessage).build());
    }

    ApiExceptionDTO apiException = ApiExceptionDTO.builder()
        .type(ApiExceptionType.MALFORMED_REQUEST.getDescription())
        .details(detailList)
        .build();
    return new ResponseEntity<>(apiException, ApiExceptionType.MALFORMED_REQUEST.getHttpStatus());
  };

  public static Function<ApiException, ResponseEntity<ApiExceptionDTO>> handleApiException = exception -> {
    ApiExceptionDTO apiException = ApiExceptionDTO.builder()
        .type(exception.getType())
        .message(exception.getMessage())
        .errorCode(exception.getErrorCode())
        .details(exception.getDetails())
        .build();
    return new ResponseEntity<>(apiException, exception.getHttpStatus());
  };

}
