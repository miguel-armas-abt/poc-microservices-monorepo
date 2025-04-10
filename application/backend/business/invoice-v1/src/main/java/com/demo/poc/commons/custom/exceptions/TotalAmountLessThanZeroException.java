package com.demo.poc.commons.custom.exceptions;

import com.demo.poc.commons.core.errors.exceptions.GenericException;
import lombok.Getter;

@Getter
public class TotalAmountLessThanZeroException extends GenericException {

  public TotalAmountLessThanZeroException() {
    super(ErrorDictionary.TOTAL_AMOUNT_LESS_THAN_ZERO.getMessage(), ErrorDictionary.parse(TotalAmountLessThanZeroException.class));
  }
}
