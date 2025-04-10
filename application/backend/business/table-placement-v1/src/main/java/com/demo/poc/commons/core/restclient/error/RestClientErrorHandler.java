package com.demo.poc.commons.core.restclient.error;

import com.demo.poc.commons.core.errors.dto.ErrorDto;
import com.demo.poc.commons.core.errors.dto.ErrorType;
import com.demo.poc.commons.core.errors.exceptions.RestClientException;
import com.demo.poc.commons.core.errors.exceptions.NoSuchRestClientErrorExtractorException;
import com.demo.poc.commons.core.errors.selector.RestClientErrorSelector;
import com.demo.poc.commons.core.properties.ConfigurationBaseProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.demo.poc.commons.core.errors.selector.RestClientErrorSelector.getDefaultResponse;
import static com.demo.poc.commons.core.errors.selector.RestClientErrorSelector.selectType;

@Slf4j
@RequiredArgsConstructor
public class RestClientErrorHandler {

  private final List<RestClientErrorExtractor> restClientErrorExtractors;
  private final RestClientErrorSelector restClientErrorSelector;
  private final ConfigurationBaseProperties properties;

  public Mono<RestClientException> handleError(ClientResponse clientResponse,
                                               Class<?> errorWrapperClass,
                                               String serviceName) {
    ErrorDto defaultError = ErrorDto.getDefaultError(properties);
    return clientResponse
        .bodyToMono(String.class)
        .flatMap(jsonBody -> Strings.EMPTY.equals(jsonBody)
            ? Mono.just(emptyResponse(defaultError))
            : Mono.just(selectExtractor(errorWrapperClass).getCodeAndMessage(jsonBody).orElseGet(() -> getDefaultResponse(defaultError, "No such external error wrapper"))))
        .switchIfEmpty(Mono.just(emptyResponse(defaultError)))
        .flatMap(codeAndMessage -> {

          String selectedCode = restClientErrorSelector.selectCode(codeAndMessage.getLeft(), serviceName);
          String selectedMessage = restClientErrorSelector.selectMessage(selectedCode, codeAndMessage.getRight(), serviceName);
          ErrorType selectedErrorType = selectType(errorWrapperClass);
          HttpStatusCode selectedHttpStatus = HttpStatusCode.valueOf(restClientErrorSelector.selectHttpCode(clientResponse.statusCode().value(), errorWrapperClass, codeAndMessage.getLeft(), serviceName));

          return Mono.error(new RestClientException(selectedCode, selectedMessage, selectedErrorType, selectedHttpStatus));
        });
  }

  private RestClientErrorExtractor selectExtractor(Class<?> errorWrapperClass) {
    return restClientErrorExtractors
        .stream()
        .filter(service -> service.supports(errorWrapperClass))
        .findFirst()
        .orElseThrow(NoSuchRestClientErrorExtractorException::new);
  }

  private Pair<String, String> emptyResponse(ErrorDto defaultError) {
    return getDefaultResponse(defaultError, "Empty response");
  }

}