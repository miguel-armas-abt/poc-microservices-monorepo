package com.demo.service.commons.exceptions;

import com.demo.commons.errors.exceptions.GenericException;
import lombok.Getter;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Getter
public class TableNotFoundException extends GenericException {

  private static final String EXCEPTION_CODE = "08.02.02";
  
  public TableNotFoundException() {
    super(
        EXCEPTION_CODE,
        "The table does not exist",
        NOT_FOUND);
  }
}