package com.demo.poc.commons.custom.exceptions;

import com.demo.poc.commons.core.errors.dto.ErrorType;
import com.demo.poc.commons.core.errors.exceptions.GenericException;
import com.demo.poc.commons.core.errors.exceptions.InvalidFieldException;
import com.demo.poc.commons.core.errors.exceptions.InvalidStreamingData;
import com.demo.poc.commons.core.errors.exceptions.JsonReadException;
import com.demo.poc.commons.core.errors.exceptions.NoSuchLoggerTypeException;
import com.demo.poc.commons.core.errors.exceptions.NoSuchParamMapperException;
import com.demo.poc.commons.core.errors.exceptions.NoSuchRestClientErrorExtractorException;
import com.demo.poc.commons.core.errors.exceptions.NoSuchRestClientException;
import com.demo.poc.commons.core.errors.exceptions.UnexpectedSslException;
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

  //system=02.00.xx
  INVALID_FIELD("02.00.01", "Invalid field", BUSINESS, BAD_REQUEST, InvalidFieldException.class),
  NO_SUCH_REST_CLIENT_ERROR_EXTRACTOR("02.00.02", "No such rest client error extractor", SYSTEM, INTERNAL_SERVER_ERROR, NoSuchRestClientErrorExtractorException.class),
  NO_SUCH_LOGGER_TYPE("02.00.03", "No such logger type", SYSTEM, INTERNAL_SERVER_ERROR, NoSuchLoggerTypeException.class),
  NO_SUCH_REST_CLIENT("02.00.04", "No such rest client", SYSTEM, INTERNAL_SERVER_ERROR, NoSuchRestClientException.class),
  ERROR_READING_JSON("02.00.05", "Error reading JSON", SYSTEM, INTERNAL_SERVER_ERROR, JsonReadException.class),
  NO_SUCH_PARAM_MAPPER("05.00.06", "No such param mapper", BUSINESS, BAD_REQUEST, NoSuchParamMapperException.class),
  INVALID_STREAMING_DATA("02.00.07", "Streaming data is not processable", SYSTEM, INTERNAL_SERVER_ERROR, InvalidStreamingData.class),
  UNEXPECTED_SSL_EXCEPTION("02.00.08", "Unexpected SSL error for HTTP client", SYSTEM, INTERNAL_SERVER_ERROR, UnexpectedSslException.class),

  //api-gateway=02.01.xx
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
