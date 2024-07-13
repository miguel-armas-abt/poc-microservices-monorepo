package com.demo.bbq.commons.errors.handler.external;

import com.demo.bbq.commons.errors.dto.ErrorDTO;
import com.demo.bbq.commons.errors.dto.ErrorType;
import com.demo.bbq.commons.errors.handler.external.strategy.ExternalErrorWrapper;
import com.demo.bbq.commons.properties.ConfigurationBaseProperties;
import com.demo.bbq.commons.properties.dto.restclient.RestClient;
import com.demo.bbq.commons.properties.dto.restclient.RestClientError;
import java.util.Map;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExternalErrorHandlerBaseUtil {

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

    return (errorWrapperClass.isAssignableFrom(ErrorDTO.class))
        ? httpCode
        : findErrors(properties, serviceName)
        .map(errors -> {
          RestClientError defaultError = RestClientError.builder().httpCode(HTTP_CONFLICT_CODE).build();
          return Optional.of(errors)
              .map(error -> error.getOrDefault(errorCode, defaultError))
              .map(RestClientError::getHttpCode)
              .orElseGet(() -> HTTP_CONFLICT_CODE);
        })
        .orElseGet(() -> HTTP_CONFLICT_CODE);
  }

  public static Optional<Map<String, RestClientError>> findErrors(ConfigurationBaseProperties properties, String serviceName) {
    return Optional.ofNullable(properties.getRestClients().get(serviceName))
        .map(RestClient::getErrors);
  }

  public static Pair<String, String> emptyResponse(ErrorDTO defaultError) {
    log.warn("Empty response body");
    return Pair.of(ErrorDTO.CODE_EMPTY, defaultError.getMessage());
  }

  public static Pair<String, String> noSuchWrapper(ErrorDTO defaultError) {
    return Pair.of("NoSuchExternalErrorWrapper", defaultError.getMessage());
  }
}