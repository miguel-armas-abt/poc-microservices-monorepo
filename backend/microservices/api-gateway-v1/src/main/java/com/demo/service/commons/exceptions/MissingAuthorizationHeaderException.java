package com.demo.service.commons.exceptions;

import com.demo.commons.errors.exceptions.GenericException;
import lombok.Getter;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Getter
public class MissingAuthorizationHeaderException extends GenericException {

  private static final String EXCEPTION_CODE = "02.02.03";

  public MissingAuthorizationHeaderException() {
    super(
        EXCEPTION_CODE,
        "Missing Authorization header",
        UNAUTHORIZED
    );
  }
}
