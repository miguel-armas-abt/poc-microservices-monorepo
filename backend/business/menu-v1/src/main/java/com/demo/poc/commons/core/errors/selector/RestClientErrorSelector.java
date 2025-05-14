package com.demo.poc.commons.core.errors.selector;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import com.demo.poc.commons.core.errors.dto.ErrorDto;
import com.demo.poc.commons.core.errors.dto.ErrorOrigin;
import com.demo.poc.commons.core.properties.ConfigurationBaseProperties;
import com.demo.poc.commons.core.properties.restclient.RestClient;
import com.demo.poc.commons.core.properties.restclient.RestClientError;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

@Slf4j
@RequiredArgsConstructor
public class RestClientErrorSelector {

  private static final String[] HTTP_ALLOWED_CODES = {"400", "401", "403", "404"};
  private static final int HTTP_CONFLICT_CODE = 409;

  private final ConfigurationBaseProperties properties;

  public String selectCode(String errorCode, String serviceName) {
    String code = Optional.ofNullable(errorCode).orElseGet(() -> ErrorDto.CODE_DEFAULT);

    return findErrors(serviceName)
        .map(errors -> Optional.of(errors)
            .filter(error -> Objects.nonNull(error.get(errorCode)))
            .map(error -> error.get(code).customCode())
            .orElse(code))
        .orElse(code);
  }

  public String selectMessage(String errorCode,
                              String errorMessage,
                              String serviceName) {
    String defaultMessage = ErrorDto.getDefaultError(properties).getMessage();
    String message = Optional.ofNullable(errorMessage).orElseGet(() -> defaultMessage);

    return findErrors(serviceName)
        .map(errors -> Optional.of(errors)
            .filter(error -> Objects.nonNull(error.get(errorCode)))
            .map(error -> error.get(errorCode))
            .map(RestClientError::message)
            .orElse(message))
        .orElse(message);
  }

  public static ErrorOrigin selectType(Class<?> errorWrapperClass) {
    return (errorWrapperClass.isAssignableFrom(ErrorDto.class))
        ? ErrorOrigin.PARTNER
        : ErrorOrigin.THIRD_PARTY;
  }

  public int selectHttpCode(int httpCode,
                            Class<?> errorWrapperClass,
                            String errorCode,
                            String serviceName) {

    if (errorWrapperClass.isAssignableFrom(ErrorDto.class))
      return httpCode;

    if (Arrays.asList(HTTP_ALLOWED_CODES).contains(String.valueOf(httpCode)))
      return httpCode;

    return findErrors(serviceName)
        .filter(errors -> Objects.nonNull(errors.get(errorCode)))
        .map(errors -> errors.get(errorCode))
        .map(RestClientError::httpCode)
        .orElse(HTTP_CONFLICT_CODE);
  }

  public Optional<Map<String, RestClientError>> findErrors(String serviceName) {
    return Optional.ofNullable(properties.restClients().get(serviceName))
        .map(RestClient::errors);
  }

  public static Pair<String, String> getDefaultResponse(ErrorDto defaultError, String logMessage) {
    log.warn(logMessage);
    return Pair.of(ErrorDto.CODE_DEFAULT, defaultError.getMessage());
  }
}