package com.demo.bbq.commons.core.errors.handler.external;

import com.demo.bbq.commons.core.errors.dto.ErrorDTO;
import com.demo.bbq.commons.core.errors.dto.ErrorType;
import com.demo.bbq.commons.core.errors.handler.external.strategy.ExternalErrorWrapper;
import com.demo.bbq.commons.core.properties.ConfigurationBaseProperties;
import com.demo.bbq.commons.core.properties.restclient.RestClient;
import com.demo.bbq.commons.core.properties.restclient.RestClientError;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExternalErrorSelector {

  private static final String[] HTTP_ALLOWED_CODES = {"400", "401"};
  private static final int HTTP_CONFLICT_CODE = 409;

  public static String selectCode(ConfigurationBaseProperties properties,
                                  String errorCode,
                                  String serviceName) {
    String code = Optional.ofNullable(errorCode).orElseGet(() -> ErrorDTO.CODE_DEFAULT);
    RestClientError defaultError = RestClientError.builder().customCode(code).build();

    return findErrors(properties, serviceName)
        .map(errors -> Optional.of(errors)
            .map(error -> error.getOrDefault(code, defaultError).getCustomCode())
            .orElseGet(() -> code))
        .orElseGet(() -> code);
  }

  public static String selectMessage(ConfigurationBaseProperties properties,
                                     String errorCode,
                                     String errorMessage,
                                     String serviceName) {
    String defaultMessage = ErrorDTO.getDefaultError(properties).getMessage();
    RestClientError defaultError = RestClientError.builder().message(defaultMessage).build();
    String message = Optional.ofNullable(errorMessage).orElseGet(() -> defaultMessage);

    return findErrors(properties, serviceName)
        .map(errors -> Optional.of(errors)
            .map(error -> error.getOrDefault(errorCode, defaultError))
            .map(RestClientError::getMessage)
            .orElseGet(() -> message))
        .orElseGet(() -> message);
  }

  public static ErrorType selectType(Class<? extends ExternalErrorWrapper> errorWrapperClass) {
    return (errorWrapperClass.isAssignableFrom(ErrorDTO.class))
        ? ErrorType.FORWARDED
        : ErrorType.EXTERNAL;
  }

  public static int selectHttpCode(ConfigurationBaseProperties properties,
                                   int httpCode,
                                   Class<?> errorWrapperClass,
                                   String errorCode,
                                   String serviceName) {

    if (errorWrapperClass.isAssignableFrom(ErrorDTO.class))
      return httpCode;

    if (Arrays.asList(HTTP_ALLOWED_CODES).contains(String.valueOf(httpCode)))
      return httpCode;

    return findErrors(properties, serviceName)
        .map(errors -> errors.getOrDefault(errorCode, RestClientError.builder().httpCode(httpCode).build()))
        .map(RestClientError::getHttpCode)
        .orElse(HTTP_CONFLICT_CODE);
  }

  public static Optional<Map<String, RestClientError>> findErrors(ConfigurationBaseProperties properties, String serviceName) {
    return Optional.ofNullable(properties.getRestClients().get(serviceName))
        .map(RestClient::getErrors);
  }

  public static Pair<String, String> getDefaultResponse(ErrorDTO defaultError, String logMessage) {
    log.warn(logMessage);
    return Pair.of(ErrorDTO.CODE_DEFAULT, defaultError.getMessage());
  }
}