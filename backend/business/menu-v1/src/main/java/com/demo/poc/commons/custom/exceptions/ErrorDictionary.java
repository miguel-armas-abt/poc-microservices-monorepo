package com.demo.poc.commons.custom.exceptions;

import com.demo.poc.commons.core.errors.exceptions.FileReadException;
import com.demo.poc.commons.core.errors.exceptions.GenericException;
import com.demo.poc.commons.core.errors.exceptions.InvalidFieldException;
import com.demo.poc.commons.core.errors.exceptions.JsonReadException;
import com.demo.poc.commons.core.errors.exceptions.NoSuchRestClientErrorExtractorException;
import com.demo.poc.commons.core.errors.exceptions.NoSuchRestClientException;
import com.demo.poc.commons.core.errors.exceptions.NoSuchParamMapperException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;

import java.util.Arrays;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Getter
@RequiredArgsConstructor
public enum ErrorDictionary {

  //system: 05.00.xx
  ERROR_READING_JSON("05.00.01", "Error reading JSON", INTERNAL_SERVER_ERROR, JsonReadException.class),
  ERROR_READING_FILE("05.00.02", "Error reading JSON", INTERNAL_SERVER_ERROR, FileReadException.class),

  //no such properties and components: 05.01.xx
  NO_SUCH_REST_CLIENT("05.01.01", "No such rest client", INTERNAL_SERVER_ERROR, NoSuchRestClientException.class),
  NO_SUCH_REST_CLIENT_ERROR_EXTRACTOR("05.01.02", "No such rest client error extractor", INTERNAL_SERVER_ERROR, NoSuchRestClientErrorExtractorException.class),
  NO_SUCH_PARAM_MAPPER("05.01.03", "No such param mapper", INTERNAL_SERVER_ERROR, NoSuchParamMapperException.class),

  //business and bad requests: 05.02.xx
  INVALID_FIELD("05.02.01", "Invalid field", BAD_REQUEST, InvalidFieldException.class),
  MENU_OPTION_NOT_FOUND("05.02.02", "The menu option does not exist", NOT_FOUND, MenuOptionNotFoundException.class),
  INVALID_MENU_CATEGORY("05.02.03", "The menu category is not defined", BAD_REQUEST, InvalidMenuCategoryException.class),;

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
