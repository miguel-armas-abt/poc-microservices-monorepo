package com.demo.service.commons.exceptions;

import com.demo.commons.errors.exceptions.GenericException;
import lombok.Getter;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Getter
public class TotalAmountLessThanZeroException extends GenericException {

  private static final String EXCEPTION_CODE = "08.02.03";

  public TotalAmountLessThanZeroException() {
    super(
        EXCEPTION_CODE,
        "Total amount less than zero",
        BAD_REQUEST);
  }
}
