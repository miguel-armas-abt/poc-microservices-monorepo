package com.demo.service.commons.exceptions;

import com.demo.commons.errors.exceptions.GenericException;
import lombok.Getter;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Getter
public class RoleNotFoundException extends GenericException {

  private static final String EXCEPTION_CODE = "02.02.03";

  public RoleNotFoundException() {
    super(
        EXCEPTION_CODE,
        "Expected role not found",
        UNAUTHORIZED);
  }
}
