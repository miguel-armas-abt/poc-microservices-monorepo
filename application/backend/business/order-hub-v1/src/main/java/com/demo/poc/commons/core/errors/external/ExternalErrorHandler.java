package com.demo.poc.commons.core.errors.external;

import com.demo.poc.commons.core.errors.dto.ErrorDto;
import com.demo.poc.commons.core.errors.dto.ErrorType;
import com.demo.poc.commons.core.errors.exceptions.ExternalServiceException;
import com.demo.poc.commons.core.errors.exceptions.NoSuchRestClientErrorStrategyException;
import com.demo.poc.commons.core.errors.external.strategy.ExternalErrorWrapper;
import com.demo.poc.commons.core.errors.external.strategy.RestClientErrorStrategy;
import com.demo.poc.commons.core.properties.ConfigurationBaseProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.demo.poc.commons.core.errors.external.ExternalErrorSelector.getDefaultResponse;
import static com.demo.poc.commons.core.errors.external.ExternalErrorSelector.selectCode;
import static com.demo.poc.commons.core.errors.external.ExternalErrorSelector.selectHttpCode;
import static com.demo.poc.commons.core.errors.external.ExternalErrorSelector.selectMessage;
import static com.demo.poc.commons.core.errors.external.ExternalErrorSelector.selectType;

@Slf4j
@RequiredArgsConstructor
public class ExternalErrorHandler {

  private final List<RestClientErrorStrategy> strategies;
  private final ConfigurationBaseProperties properties;

  public Mono<ExternalServiceException> handleError(ClientResponse clientResponse,
                                                    Class<? extends ExternalErrorWrapper> errorWrapperClass,
                                                    String serviceName) {
    ErrorDto defaultError = ErrorDto.getDefaultError(properties);
    return clientResponse
        .bodyToMono(String.class)
        .flatMap(jsonBody -> Strings.EMPTY.equals(jsonBody)
            ? Mono.just(emptyResponse(defaultError))
            : Mono.just(selectStrategy(errorWrapperClass).getCodeAndMessage(jsonBody).orElseGet(() -> getDefaultResponse(defaultError, "No such external error wrapper"))))
        .switchIfEmpty(Mono.just(emptyResponse(defaultError)))
        .flatMap(codeAndMessage -> {

          String selectedCode = selectCode(properties, codeAndMessage.getLeft(), serviceName);
          String selectedMessage = selectMessage(properties, selectedCode, codeAndMessage.getRight(), serviceName);
          ErrorType selectedErrorType = selectType(errorWrapperClass);
          HttpStatusCode selectedHttpStatus = HttpStatusCode.valueOf(selectHttpCode(properties, clientResponse.statusCode().value(), errorWrapperClass, codeAndMessage.getLeft(), serviceName));

          return Mono.error(new ExternalServiceException(selectedCode, selectedMessage, selectedErrorType, selectedHttpStatus));
        });
  }

  private RestClientErrorStrategy selectStrategy(Class<? extends ExternalErrorWrapper> errorWrapperClass) {
    return strategies
        .stream()
        .filter(service -> service.supports(errorWrapperClass))
        .findFirst()
        .orElseThrow(NoSuchRestClientErrorStrategyException::new);
  }

  private Pair<String, String> emptyResponse(ErrorDto defaultError) {
    return getDefaultResponse(defaultError, "Empty response");
  }

}