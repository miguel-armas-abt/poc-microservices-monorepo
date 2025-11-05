package com.demo.service.commons.custom.exceptions;

import com.demo.commons.errors.exceptions.GenericException;
import lombok.Getter;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Getter
public class InvalidJwtException extends GenericException {

  private static final String EXCEPTION_CODE = "03.02.02";

  public InvalidJwtException(String message) {
    super(
        EXCEPTION_CODE,
        "Invalid JWT",
        UNAUTHORIZED
    );
  }
}
