package com.demo.poc.commons.custom.exceptions;

import com.demo.poc.commons.core.errors.dto.ErrorType;
import com.demo.poc.commons.core.errors.exceptions.EmptyBaseUrlException;
import com.demo.poc.commons.core.errors.exceptions.GenericException;
import com.demo.poc.commons.core.errors.exceptions.InvalidFieldException;
import com.demo.poc.commons.core.errors.exceptions.JsonReadException;
import com.demo.poc.commons.core.errors.exceptions.NoSuchParamMapperException;
import com.demo.poc.commons.core.errors.exceptions.NoSuchRestClientErrorExtractorException;
import com.demo.poc.commons.core.errors.exceptions.NoSuchRestClientException;
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

  //system=03.00.xx
  ERROR_READING_JSON("03.00.01", "Error reading JSON", SYSTEM, INTERNAL_SERVER_ERROR, JsonReadException.class),

  //no such properties and components: 03.01.xx
  NO_SUCH_REST_CLIENT("03.01.01", "No such rest client", SYSTEM, INTERNAL_SERVER_ERROR, NoSuchRestClientException.class),
  NO_SUCH_REST_CLIENT_ERROR_EXTRACTOR("03.01.02", "No such rest client error extractor", SYSTEM, INTERNAL_SERVER_ERROR, NoSuchRestClientErrorExtractorException.class),
  NO_SUCH_PARAM_MAPPER("03.01.03", "No such param mapper", SYSTEM, BAD_REQUEST, NoSuchParamMapperException.class),
  EMPTY_BASE_URL("03.01.04", "Base URL is required", SYSTEM, INTERNAL_SERVER_ERROR, EmptyBaseUrlException.class),

  //business and bad requests: 03.02.xx
  INVALID_FIELD("03.02.01", "Invalid field", BUSINESS, BAD_REQUEST, InvalidFieldException.class),
  UNABLE_LOGOUT("03.02.02", "Unable logout", BUSINESS, UNAUTHORIZED, UnableLogoutException.class),
  UNABLE_REFRESH("03.02.03", "Unable refresh", BUSINESS, UNAUTHORIZED, UnableRefreshException.class),
  INVALID_JWT("03.02.04", "Invalid JWT", BUSINESS, UNAUTHORIZED, InvalidJwtException.class),
  EXPIRED_TOKEN("03.02.05", "Expired token", BUSINESS, UNAUTHORIZED, ExpiredTokenException.class),;

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
