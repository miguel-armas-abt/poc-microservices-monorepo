package com.demo.bbq.commons.errors.handler.external;

import com.demo.bbq.commons.errors.dto.ErrorDTO;
import com.demo.bbq.commons.errors.dto.ErrorType;
import com.demo.bbq.commons.errors.handler.external.strategy.ExternalErrorWrapper;
import com.demo.bbq.commons.properties.ConfigurationBaseProperties;
import com.demo.bbq.commons.properties.dto.RestClient;
import com.demo.bbq.commons.properties.dto.RestClientError;
import java.util.Map;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExternalErrorHandlerBaseUtil {

  private static final int HTTP_CONFLICT_CODE = 409;

  public static String selectCode(ConfigurationBaseProperties properties,
                            String errorCode,
                            String serviceName) {

    return findErrors(properties, serviceName)
        .map(errors -> {
          RestClientError defaultError = RestClientError.builder().code(errorCode).build();
          return Optional.of(errors)
              .map(error -> error.getOrDefault(errorCode, defaultError).getCode())
              .orElseGet(() -> errorCode);
        })
        .orElseGet(() -> errorCode);
  }

  public static String selectMessage(ConfigurationBaseProperties properties,
                                     String errorCode,
                                     String errorMessage,
                                     String serviceName) {

    return findErrors(properties, serviceName)
        .map(errors -> {
          RestClientError defaultError = RestClientError.builder().message(ErrorDTO.getDefaultError(properties).getMessage()).build();

          return Optional.of(errors)
              .map(error -> error.getOrDefault(errorCode, defaultError).getMessage())
              .orElseGet(defaultError::getMessage);
        })
        .orElseGet(() -> errorMessage);
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
        : ExternalErrorHandlerBaseUtil.findErrors(properties, serviceName)
        .map(errors -> {
          RestClientError defaultError = RestClientError.builder().httpCode(HTTP_CONFLICT_CODE).build();
          return Optional.of(errors)
              .map(error -> error.getOrDefault(errorCode, defaultError).getHttpCode())
              .orElseGet(() -> HTTP_CONFLICT_CODE);
        })
        .orElseGet(() -> HTTP_CONFLICT_CODE);
  }

  public static Optional<Map<String, RestClientError>> findErrors(ConfigurationBaseProperties properties, String serviceName) {
    return Optional.ofNullable(properties.getRestClients().get(serviceName))
        .map(RestClient::getErrors);
  }
}