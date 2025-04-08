package com.demo.poc.commons.core.errors.exceptions;

import com.demo.poc.commons.core.errors.dto.ErrorDTO;
import com.demo.poc.commons.core.errors.enums.ErrorDictionary;
import lombok.Getter;

@Getter
public class NoSuchRestClientException extends GenericException {

  public NoSuchRestClientException() {
    super(ErrorDictionary.NO_SUCH_REST_CLIENT.getMessage());

    ErrorDictionary detail = ErrorDictionary.parse(this.getClass());
    this.httpStatus = detail.getHttpStatus();
    this.errorDetail = ErrorDTO.builder()
            .type(detail.getType())
            .code(detail.getCode())
            .message(detail.getMessage())
            .build();
  }
}