package com.demo.bbq.utils.errors.handler.external;

import com.demo.bbq.utils.errors.exceptions.ExternalServiceException;
import com.demo.bbq.utils.errors.handler.external.service.WebfluxClientErrorService;
import com.demo.bbq.utils.properties.ConfigurationBaseProperties;
import java.util.List;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

public class ExternalClientErrorUtil {

  private ExternalClientErrorUtil() {}

  public static Mono<ExternalServiceException> handleError(ClientResponse clientResponse,
                                                      Class<?> errorWrapperClass,
                                                      String serviceName,
                                                      List<WebfluxClientErrorService> serviceList,
                                                      ConfigurationBaseProperties properties) {

    return selectService(errorWrapperClass, serviceList)
        .getCodeAndMessage(clientResponse)
        .flatMap(codeAndMessage -> {
          Pair<String, String> pairCodeAndMessage = (Pair<String, String>) codeAndMessage;
          String code = pairCodeAndMessage.getLeft();
          String message = pairCodeAndMessage.getRight();
          String selectedMessage = selectMessage(properties, code, message, serviceName);
          return Mono.error(new ExternalServiceException(code, selectedMessage, clientResponse.statusCode()));
        });
  }

  private static String selectMessage(ConfigurationBaseProperties properties,
                                      String errorCode,
                                      String errorMessage,
                                      String serviceName) {

    boolean showMessage = properties.isShowCustomMessages();
    return showMessage ? properties.getRestClients().get(serviceName).getErrors().get(errorCode) : errorMessage;
  }

  private static WebfluxClientErrorService selectService(Class<?> errorWrapperClass, List<WebfluxClientErrorService> serviceList) {
    return serviceList
        .stream()
        .filter(service -> service.supports(errorWrapperClass))
        .findFirst()
        .orElseThrow();
  }

}
