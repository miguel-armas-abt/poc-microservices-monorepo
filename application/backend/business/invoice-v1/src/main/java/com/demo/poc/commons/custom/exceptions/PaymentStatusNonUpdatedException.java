package com.demo.poc.commons.custom.exceptions;

import com.demo.poc.commons.core.errors.dto.ErrorDTO;
import com.demo.poc.commons.core.errors.enums.ErrorDictionary;
import com.demo.poc.commons.core.errors.exceptions.GenericException;
import lombok.Getter;

@Getter
public class PaymentStatusNonUpdatedException extends GenericException {

  public PaymentStatusNonUpdatedException() {
    super(ErrorDictionary.PAYMENT_STATUS_NON_UPDATED_EXCEPTION.getMessage());

    ErrorDictionary detail = ErrorDictionary.parse(this.getClass());
    this.httpStatus = detail.getHttpStatus();
    this.errorDetail = ErrorDTO.builder()
            .type(detail.getType())
            .code(detail.getCode())
            .message(detail.getMessage())
            .build();
  }
}
