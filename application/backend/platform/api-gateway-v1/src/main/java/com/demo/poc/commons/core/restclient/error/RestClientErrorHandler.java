package com.demo.poc.commons.core.restclient.error;

import com.demo.poc.commons.core.errors.dto.ErrorDto;
import com.demo.poc.commons.core.errors.dto.ErrorType;
import com.demo.poc.commons.core.errors.exceptions.RestClientException;
import com.demo.poc.commons.core.errors.exceptions.NoSuchRestClientErrorExtractorException;
import com.demo.poc.commons.core.errors.selector.RestClientErrorSelector;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

import static com.demo.poc.commons.core.errors.selector.RestClientErrorSelector.selectType;

@Slf4j
@RequiredArgsConstructor
public class RestClientErrorHandler {

  private static final String EMPTY_RESPONSE = "REST Client empty response body";
  private static final String NO_SUCH_ERROR_WRAPPER = "No such error wrapper for REST client error response";

  private final List<RestClientErrorExtractor> restClientErrorExtractors;
  private final RestClientErrorSelector restClientErrorSelector;

  public Mono<RestClientException> handleError(ClientResponse clientResponse,
                                               Class<?> errorWrapperClass,
                                               String serviceName) {
    return clientResponse
        .bodyToMono(String.class)
        .flatMap(jsonBody -> Mono.just(getCodeAndMessage(jsonBody, errorWrapperClass)))
        .switchIfEmpty(Mono.just(mapUnexpectedResponse(EMPTY_RESPONSE)))
        .flatMap(codeAndMessage -> {
          String extractedCode = codeAndMessage.getKey();
          String extractedMessage = codeAndMessage.getValue();

          String selectedCode = restClientErrorSelector.selectCode(extractedCode, serviceName);
          String selectedMessage = restClientErrorSelector.selectMessage(selectedCode, extractedMessage, serviceName);
          ErrorType selectedErrorType = selectType(errorWrapperClass);
          HttpStatusCode selectedHttpStatus = HttpStatusCode.valueOf(restClientErrorSelector.selectHttpCode(clientResponse.statusCode().value(), errorWrapperClass, extractedCode, serviceName));

          return Mono.error(new RestClientException(selectedCode, selectedMessage, selectedErrorType, selectedHttpStatus));
        });
  }

  private Map.Entry<String, String> getCodeAndMessage(String jsonBody, Class<?> errorWrapperClass) {
    if (Strings.EMPTY.equals(jsonBody)) {
      return mapUnexpectedResponse(EMPTY_RESPONSE);

    } else {
      return selectExtractor(errorWrapperClass)
          .getCodeAndMessage(jsonBody)
          .orElseGet(() -> {
            log.warn(jsonBody);
            return mapUnexpectedResponse(NO_SUCH_ERROR_WRAPPER);
          });
    }
  }

  private Map.Entry<String, String> mapUnexpectedResponse(String message) {
    return Map.of(ErrorDto.CODE_DEFAULT, message).entrySet().iterator().next();
  }

  private RestClientErrorExtractor selectExtractor(Class<?> errorWrapperClass) {
    return restClientErrorExtractors
        .stream()
        .filter(service -> service.supports(errorWrapperClass))
        .findFirst()
        .orElseThrow(() -> new NoSuchRestClientErrorExtractorException(errorWrapperClass));
  }
}