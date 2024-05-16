package com.demo.bbq.utils.errors.handler.external;

import static com.demo.bbq.utils.errors.handler.external.ExternalErrorHandlerBaseUtil.selectCode;
import static com.demo.bbq.utils.errors.handler.external.ExternalErrorHandlerBaseUtil.selectMessage;
import static com.demo.bbq.utils.errors.handler.external.ExternalErrorHandlerBaseUtil.selectType;
import static com.demo.bbq.utils.errors.handler.external.ExternalErrorHandlerBaseUtil.selectHttpCode;

import com.demo.bbq.utils.errors.dto.ErrorDTO;
import com.demo.bbq.utils.errors.dto.ErrorType;
import com.demo.bbq.utils.errors.exceptions.ExternalServiceException;
import com.demo.bbq.utils.errors.handler.external.strategy.ExternalErrorWrapper;
import com.demo.bbq.utils.errors.handler.external.strategy.RestClientErrorStrategy;
import com.demo.bbq.utils.properties.ConfigurationBaseProperties;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExternalErrorHandlerUtil {

  public static Mono<ExternalServiceException> build(ClientResponse clientResponse,
                                                     Class<? extends ExternalErrorWrapper> errorWrapperClass,
                                                     String serviceName,
                                                     List<RestClientErrorStrategy> serviceList,
                                                     ConfigurationBaseProperties properties) {
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