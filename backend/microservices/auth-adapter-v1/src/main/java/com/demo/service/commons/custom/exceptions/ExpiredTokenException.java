package com.demo.service.commons.custom.exceptions;

import com.demo.commons.errors.exceptions.GenericException;
import lombok.Getter;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Getter
public class ExpiredTokenException extends GenericException {

  private static final String EXCEPTION_CODE = "10.01.09";

  public ExpiredTokenException() {
    super(
        EXCEPTION_CODE,
        "Expired token",
        UNAUTHORIZED
    );
  }
}
