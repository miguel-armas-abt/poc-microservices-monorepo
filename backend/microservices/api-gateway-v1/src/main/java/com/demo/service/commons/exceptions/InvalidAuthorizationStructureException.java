package com.demo.service.commons.exceptions;

import com.demo.commons.errors.exceptions.GenericException;
import lombok.Getter;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Getter
public class InvalidAuthorizationStructureException extends GenericException {

  private static final String EXCEPTION_CODE = "02.02.04";

  public InvalidAuthorizationStructureException() {
    super(
        EXCEPTION_CODE,
        "Invalid Authorization structure",
        UNAUTHORIZED
    );
  }
}
