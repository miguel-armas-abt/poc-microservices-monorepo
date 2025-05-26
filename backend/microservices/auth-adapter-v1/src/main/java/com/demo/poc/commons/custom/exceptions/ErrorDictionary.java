package com.demo.poc.commons.custom.exceptions;

import com.demo.poc.commons.core.errors.exceptions.GenericException;
import com.demo.poc.commons.core.errors.exceptions.InvalidFieldException;
import com.demo.poc.commons.core.errors.exceptions.InvalidStreamingData;
import com.demo.poc.commons.core.errors.exceptions.JsonReadException;
import com.demo.poc.commons.core.errors.exceptions.NoSuchParamMapperException;
import com.demo.poc.commons.core.errors.exceptions.NoSuchRestClientErrorExtractorException;
import com.demo.poc.commons.core.errors.exceptions.NoSuchRestClientException;
import com.demo.poc.commons.core.errors.exceptions.UnexpectedSslException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;

import java.util.Arrays;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Getter
@RequiredArgsConstructor
public enum ErrorDictionary {

  //system=03.00.xx
  UNEXPECTED_SSL_EXCEPTION("03.00.01", "Unexpected SSL error for HTTP client", INTERNAL_SERVER_ERROR, UnexpectedSslException.class),
  INVALID_STREAMING_DATA("03.00.02", "Streaming data is not processable", INTERNAL_SERVER_ERROR, InvalidStreamingData.class),
  ERROR_READING_JSON("03.00.03", "Error reading JSON", INTERNAL_SERVER_ERROR, JsonReadException.class),

  //no such properties and components: 03.01.xx
  NO_SUCH_REST_CLIENT("03.01.01", "No such rest client", INTERNAL_SERVER_ERROR, NoSuchRestClientException.class),
  NO_SUCH_REST_CLIENT_ERROR_EXTRACTOR("03.01.02", "No such rest client error extractor", INTERNAL_SERVER_ERROR, NoSuchRestClientErrorExtractorException.class),
  NO_SUCH_PARAM_MAPPER("03.01.03", "No such param mapper", INTERNAL_SERVER_ERROR, NoSuchParamMapperException.class),

  //business and bad requests: 03.02.xx
  INVALID_FIELD("03.02.01", "Invalid field", BAD_REQUEST, InvalidFieldException.class),
  INVALID_JWT("03.02.02", "Invalid JWT", UNAUTHORIZED, InvalidJwtException.class),
  EXPIRED_TOKEN("03.02.03", "Expired token", UNAUTHORIZED, ExpiredTokenException.class),;

  private final String code;
  private final String message;
  private final HttpStatusCode httpStatus;
  private final Class<? extends GenericException> exceptionClass;

  public static ErrorDictionary parse(Class<? extends GenericException> exceptionClass) {
    return Arrays.stream(ErrorDictionary.values())
            .filter(errorDetail -> errorDetail.getExceptionClass().isAssignableFrom(exceptionClass))
            .findFirst()
            .orElseThrow(() -> new GenericException("No such exception"));
  }
}
