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
import com.demo.poc.commons.custom.exceptions.TableNotFoundException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

import static com.demo.poc.commons.core.errors.dto.ErrorType.BUSINESS;
import static com.demo.poc.commons.core.errors.dto.ErrorType.SYSTEM;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Getter
@RequiredArgsConstructor
public enum ErrorDictionary {

  //system=00
  INVALID_FIELD("07.00.01", "Invalid field", BUSINESS, BAD_REQUEST, InvalidFieldException.class),
  NO_SUCH_REST_CLIENT_ERROR_STRATEGY("07.00.02", "No such rest client error strategy", SYSTEM, INTERNAL_SERVER_ERROR, NoSuchRestClientErrorStrategyException.class),
  NO_SUCH_LOGGER_TYPE("07.00.03", "No such logger type", SYSTEM, INTERNAL_SERVER_ERROR, NoSuchLoggerTypeException.class),
  NO_SUCH_REST_CLIENT("07.00.04", "No such rest client", SYSTEM, INTERNAL_SERVER_ERROR, NoSuchRestClientException.class),
  ERROR_READING_JSON("07.00.05", "Error reading JSON", SYSTEM, INTERNAL_SERVER_ERROR, JsonReadException.class),
  ERROR_MAPPING_REFLECTIVE_PARAMS("07.00.06", "Error mapping reflective params", SYSTEM, INTERNAL_SERVER_ERROR, ReflectiveParamMappingException.class),
  ERROR_ASSIGN_REFLECTIVE_PARAMS("07.00.07", "Error assign reflective params", SYSTEM, INTERNAL_SERVER_ERROR, ReflectiveParamAssignmentException.class),
  INVALID_STREAMING_DATA("07.00.08", "Streaming data is not processable", SYSTEM, INTERNAL_SERVER_ERROR, InvalidStreamingData.class),
  UNEXPECTED_SSL_EXCEPTION("07.00.09", "Unexpected SSL error for HTTP client", SYSTEM, INTERNAL_SERVER_ERROR, UnexpectedSslException.class),

  //custom=01
  TABLE_NOT_FOUND("The table does not exist", "07.01.01", BUSINESS, NOT_FOUND, TableNotFoundException.class);

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
