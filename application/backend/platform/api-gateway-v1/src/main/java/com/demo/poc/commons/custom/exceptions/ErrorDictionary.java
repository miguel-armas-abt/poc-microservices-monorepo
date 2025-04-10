package com.demo.poc.commons.custom.exceptions;

import com.demo.poc.commons.core.errors.dto.ErrorType;
import com.demo.poc.commons.core.errors.exceptions.GenericException;
import com.demo.poc.commons.core.errors.exceptions.InvalidFieldException;
import com.demo.poc.commons.core.errors.exceptions.InvalidStreamingData;
import com.demo.poc.commons.core.errors.exceptions.JsonReadException;
import com.demo.poc.commons.core.errors.exceptions.NoSuchLoggerTypeException;
import com.demo.poc.commons.core.errors.exceptions.NoSuchRestClientErrorStrategyException;
import com.demo.poc.commons.core.errors.exceptions.NoSuchRestClientException;
import com.demo.poc.commons.core.errors.exceptions.ReflectiveParamAssignmentException;
import com.demo.poc.commons.core.errors.exceptions.ReflectiveParamMappingException;
import com.demo.poc.commons.core.errors.exceptions.UnexpectedSslException;
import com.demo.poc.commons.custom.exceptions.InvalidAuthorizationStructureException;
import com.demo.poc.commons.custom.exceptions.MissingAuthorizationHeaderException;
import com.demo.poc.commons.custom.exceptions.RoleNotFoundException;
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
  INVALID_FIELD("02.00.01", "Invalid field", BUSINESS, BAD_REQUEST, InvalidFieldException.class),
  NO_SUCH_REST_CLIENT_ERROR_STRATEGY("02.00.02", "No such rest client error strategy", SYSTEM, INTERNAL_SERVER_ERROR, NoSuchRestClientErrorStrategyException.class),
  NO_SUCH_LOGGER_TYPE("02.00.03", "No such logger type", SYSTEM, INTERNAL_SERVER_ERROR, NoSuchLoggerTypeException.class),
  NO_SUCH_REST_CLIENT("02.00.04", "No such rest client", SYSTEM, INTERNAL_SERVER_ERROR, NoSuchRestClientException.class),
  ERROR_READING_JSON("02.00.05", "Error reading JSON", SYSTEM, INTERNAL_SERVER_ERROR, JsonReadException.class),
  ERROR_MAPPING_REFLECTIVE_PARAMS("02.00.06", "Error mapping reflective params", SYSTEM, INTERNAL_SERVER_ERROR, ReflectiveParamMappingException.class),
  ERROR_ASSIGN_REFLECTIVE_PARAMS("02.00.07", "Error assign reflective params", SYSTEM, INTERNAL_SERVER_ERROR, ReflectiveParamAssignmentException.class),
  INVALID_STREAMING_DATA("02.00.08", "Streaming data is not processable", SYSTEM, INTERNAL_SERVER_ERROR, InvalidStreamingData.class),
  UNEXPECTED_SSL_EXCEPTION("02.00.09", "Unexpected SSL error for HTTP client", SYSTEM, INTERNAL_SERVER_ERROR, UnexpectedSslException.class),


  //custom=01
  ROLE_NOT_FOUND("02.01.01", "Expected role not found", BUSINESS, UNAUTHORIZED, RoleNotFoundException.class),
  MISSING_AUTHORIZATION_HEADER("02.01.02", "Missing Authorization header", BUSINESS, UNAUTHORIZED, MissingAuthorizationHeaderException.class),
  INVALID_AUTHORIZATION_STRUCTURE("02.01.03", "Invalid Authorization structure", BUSINESS, UNAUTHORIZED, InvalidAuthorizationStructureException.class);

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
