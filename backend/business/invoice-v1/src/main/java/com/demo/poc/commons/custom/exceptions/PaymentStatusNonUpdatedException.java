package com.demo.poc.commons.custom.exceptions;

import com.demo.poc.commons.core.errors.exceptions.GenericException;
import lombok.Getter;

@Getter
public class PaymentStatusNonUpdatedException extends GenericException {

  public PaymentStatusNonUpdatedException() {
    super(ErrorDictionary.PAYMENT_STATUS_NON_UPDATED_EXCEPTION.getMessage(), ErrorDictionary.parse(PaymentStatusNonUpdatedException.class));
  }
}
