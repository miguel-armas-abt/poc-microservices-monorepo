package com.demo.bbq.commons.errors.handler.external;

import static com.demo.bbq.commons.errors.handler.external.ExternalErrorHandlerBaseUtil.selectCode;
import static com.demo.bbq.commons.errors.handler.external.ExternalErrorHandlerBaseUtil.selectMessage;
import static com.demo.bbq.commons.errors.handler.external.ExternalErrorHandlerBaseUtil.selectType;
import static com.demo.bbq.commons.errors.handler.external.ExternalErrorHandlerBaseUtil.selectHttpCode;

import com.demo.bbq.commons.errors.dto.ErrorDTO;
import com.demo.bbq.commons.errors.dto.ErrorType;
import com.demo.bbq.commons.errors.exceptions.ExternalServiceException;
import com.demo.bbq.commons.errors.handler.external.strategy.ExternalErrorWrapper;
import com.demo.bbq.commons.errors.handler.external.strategy.RestClientErrorStrategy;
import com.demo.bbq.commons.properties.ConfigurationBaseProperties;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ExternalErrorHandler {

  private final List<RestClientErrorStrategy> serviceList;
  private final ConfigurationBaseProperties properties;

  public Mono<ExternalServiceException> handleError(ClientResponse clientResponse,
                                                    Class<? extends ExternalErrorWrapper> errorWrapperClass,
                                                    String serviceName) {
    return selectService(errorWrapperClass, serviceList)
        .getCodeAndMessage(clientResponse)
        .switchIfEmpty(Mono.just(Pair.of(ErrorDTO.CODE_EMPTY, ErrorDTO.getDefaultError(properties).getMessage())))
        .flatMap(codeAndMessage -> {

          String selectedCode = selectCode(properties, codeAndMessage.getLeft(), serviceName);
          String selectedMessage = selectMessage(properties, selectedCode, codeAndMessage.getRight(), serviceName);
          ErrorType selectedErrorType = selectType(errorWrapperClass);
          HttpStatusCode selectedHttpStatus = HttpStatusCode.valueOf(selectHttpCode(properties, clientResponse.statusCode().value(), errorWrapperClass, codeAndMessage.getLeft(), serviceName));

          return Mono.error(new ExternalServiceException(selectedCode, selectedMessage, selectedErrorType, selectedHttpStatus));
        });
  }

  private static RestClientErrorStrategy selectService(Class<? extends ExternalErrorWrapper> errorWrapperClass,
                                                       List<RestClientErrorStrategy> serviceList) {
    return serviceList
        .stream()
        .filter(service -> service.supports(errorWrapperClass))
        .findFirst()
        .orElseThrow();
  }
}