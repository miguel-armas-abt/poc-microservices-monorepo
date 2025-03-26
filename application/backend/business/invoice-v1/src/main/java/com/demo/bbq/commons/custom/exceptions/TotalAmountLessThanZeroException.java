package com.demo.bbq.commons.custom.exceptions;

import com.demo.bbq.commons.core.errors.dto.ErrorDTO;
import com.demo.bbq.commons.core.errors.enums.ErrorDictionary;
import com.demo.bbq.commons.core.errors.exceptions.GenericException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class TotalAmountLessThanZeroException extends GenericException {

  private static final ErrorDictionary EXCEPTION = ErrorDictionary.TOTAL_AMOUNT_LESS_THAN_ZERO;

  public TotalAmountLessThanZeroException() {
    super(EXCEPTION.getMessage());
    this.httpStatus = HttpStatus.BAD_REQUEST;
    this.errorDetail = ErrorDTO.builder()
        .code(EXCEPTION.getCode())
        .message(EXCEPTION.getMessage())
        .build();
  }
}
