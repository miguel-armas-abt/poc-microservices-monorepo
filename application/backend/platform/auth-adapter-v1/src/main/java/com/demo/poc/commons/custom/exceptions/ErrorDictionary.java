package com.demo.poc.commons.custom.exceptions;

import com.demo.poc.commons.core.errors.dto.ErrorType;
import com.demo.poc.commons.core.errors.exceptions.EmptyBaseUrlException;
import com.demo.poc.commons.core.errors.exceptions.GenericException;
import com.demo.poc.commons.core.errors.exceptions.InvalidFieldException;
import com.demo.poc.commons.core.errors.exceptions.JsonReadException;
import com.demo.poc.commons.core.errors.exceptions.NoSuchLoggerTypeException;
import com.demo.poc.commons.core.errors.exceptions.NoSuchRestClientErrorExtractorException;
import com.demo.poc.commons.core.errors.exceptions.NoSuchRestClientException;
import com.demo.poc.commons.core.errors.exceptions.ReflectiveParamAssignmentException;
import com.demo.poc.commons.core.errors.exceptions.ReflectiveParamMappingException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

import static com.demo.poc.commons.core.errors.dto.ErrorType.BUSINESS;
import static com.demo.poc.commons.core.errors.dto.ErrorType.SYSTEM;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Getter
@RequiredArgsConstructor
public enum ErrorDictionary {

  //system=00
  INVALID_FIELD("03.00.01", "Invalid field", BUSINESS, BAD_REQUEST, InvalidFieldException.class),
  NO_SUCH_REST_CLIENT_ERROR_EXTRACTOR("03.00.02", "No such rest client error extractor", SYSTEM, INTERNAL_SERVER_ERROR, NoSuchRestClientErrorExtractorException.class),
  NO_SUCH_LOGGER_TYPE("03.00.03", "No such logger type", SYSTEM, INTERNAL_SERVER_ERROR, NoSuchLoggerTypeException.class),
  NO_SUCH_REST_CLIENT("03.00.04", "No such rest client", SYSTEM, INTERNAL_SERVER_ERROR, NoSuchRestClientException.class),
  ERROR_READING_JSON("03.00.05", "Error reading JSON", SYSTEM, INTERNAL_SERVER_ERROR, JsonReadException.class),
  ERROR_MAPPING_REFLECTIVE_PARAMS("03.00.06", "Error mapping reflective params", SYSTEM, INTERNAL_SERVER_ERROR, ReflectiveParamMappingException.class),
  ERROR_ASSIGN_REFLECTIVE_PARAMS("03.00.07", "Error assign reflective params", SYSTEM, INTERNAL_SERVER_ERROR, ReflectiveParamAssignmentException.class),
  EMPTY_BASE_URL("03.00.08", "Base URL is required", SYSTEM, INTERNAL_SERVER_ERROR, EmptyBaseUrlException.class),

  //custom=01
  UNABLE_LOGOUT("03.01.01", "Unable logout", BUSINESS, UNAUTHORIZED, UnableLogoutException.class),
  UNABLE_REFRESH("03.01.02", "Unable refresh", BUSINESS, UNAUTHORIZED, UnableRefreshException.class),
  INVALID_JWT("03.01.03", "Invalid JWT", BUSINESS, UNAUTHORIZED, InvalidJwtException.class),
  EXPIRED_TOKEN("03.01.04", "Expired token", BUSINESS, UNAUTHORIZED, ExpiredTokenException.class),;

  private final String code;
  private final String message;
  private final ErrorType type;
  private final HttpStatus httpStatus;
  private final Class<? extends GenericException> exceptionClass;

  public static ErrorDictionary parse(Class<? extends GenericException> exceptionClass) {
    return Arrays.stream(ErrorDictionary.values())
            .filter(errorDetail -> errorDetail.getExceptionClass().isAssignableFrom(exceptionClass))
            .findFirst()
            .orElseThrow(() -> new GenericException("No such exception"));
  }
}
