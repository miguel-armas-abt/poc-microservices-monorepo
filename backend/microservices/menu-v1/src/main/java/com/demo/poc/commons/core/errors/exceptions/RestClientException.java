package com.demo.poc.commons.core.errors.exceptions;

import com.demo.poc.commons.core.errors.dto.ErrorDto;
import com.demo.poc.commons.core.errors.dto.ErrorOrigin;
import jakarta.ws.rs.core.Response;
import lombok.Getter;

@Getter
public class RestClientException extends GenericException {

  public RestClientException(String code, String message, ErrorOrigin errorOrigin, Response.Status httpStatusCode) {
    super(message);
    this.httpStatus = httpStatusCode;
    this.errorDetail = ErrorDto.builder()
        .origin(errorOrigin)
        .code(code)
        .message(message)
        .build();
  }
}
