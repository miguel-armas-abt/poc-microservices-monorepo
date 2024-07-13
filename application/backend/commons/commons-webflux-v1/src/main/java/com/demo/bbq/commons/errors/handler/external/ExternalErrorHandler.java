package com.demo.bbq.commons.errors.handler.external;

import static com.demo.bbq.commons.errors.handler.external.ExternalErrorHandlerBaseUtil.selectCode;
import static com.demo.bbq.commons.errors.handler.external.ExternalErrorHandlerBaseUtil.selectMessage;
import static com.demo.bbq.commons.errors.handler.external.ExternalErrorHandlerBaseUtil.selectType;
import static com.demo.bbq.commons.errors.handler.external.ExternalErrorHandlerBaseUtil.selectHttpCode;
import static com.demo.bbq.commons.errors.handler.external.ExternalErrorHandlerBaseUtil.emptyResponse;
import static com.demo.bbq.commons.errors.handler.external.ExternalErrorHandlerBaseUtil.noSuchWrapper;

import com.demo.bbq.commons.errors.dto.ErrorDTO;
import com.demo.bbq.commons.errors.dto.ErrorType;
import com.demo.bbq.commons.errors.exceptions.ExternalServiceException;
import com.demo.bbq.commons.errors.exceptions.SystemException;
import com.demo.bbq.commons.errors.handler.external.strategy.ExternalErrorWrapper;
import com.demo.bbq.commons.errors.handler.external.strategy.RestClientErrorStrategy;
import com.demo.bbq.commons.properties.ConfigurationBaseProperties;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
public class ExternalErrorHandler {

  private final List<RestClientErrorStrategy> strategies;
  private final ConfigurationBaseProperties properties;

  public Mono<ExternalServiceException> handleError(ClientResponse clientResponse,
                                                    Class<? extends ExternalErrorWrapper> errorWrapperClass,
                                                    String serviceName) {
    ErrorDTO defaultError = ErrorDTO.getDefaultError(properties);
    return clientResponse
        .bodyToMono(String.class)
        .flatMap(jsonBody -> Strings.EMPTY.equals(jsonBody)
            ? Mono.just(emptyResponse(defaultError))
            : Mono.just(selectStrategy(errorWrapperClass).getCodeAndMessage(jsonBody).orElseGet(() -> noSuchWrapper(defaultError))))
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
        .orElseThrow(() -> new SystemException("NoSuchRestClientErrorStrategy"));
  }

}