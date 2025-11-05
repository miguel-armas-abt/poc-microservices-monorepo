package com.demo.service.commons.exceptions;

import com.demo.commons.errors.exceptions.GenericException;
import lombok.Getter;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Getter
public class PaymentStatusNonUpdatedException extends GenericException {

  private static final String EXCEPTION_CODE = "08.02.02";

  public PaymentStatusNonUpdatedException() {
    super(
        EXCEPTION_CODE,
        "Payment status could not be updated",
        BAD_REQUEST);
  }
}
