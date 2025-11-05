package com.demo.service.commons.exceptions;

import com.demo.commons.errors.exceptions.GenericException;
import lombok.Getter;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Getter
public class NoSuchMenuRepositoryStrategyException extends GenericException {

  private static final String EXCEPTION_CODE = "10.01.04";

  public NoSuchMenuRepositoryStrategyException() {
    super(
        EXCEPTION_CODE,
        "No such menu repository strategy",
        INTERNAL_SERVER_ERROR);
  }
}
