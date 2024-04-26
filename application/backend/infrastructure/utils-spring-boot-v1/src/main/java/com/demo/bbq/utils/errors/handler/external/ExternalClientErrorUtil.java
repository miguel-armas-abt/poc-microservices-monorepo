package com.demo.bbq.utils.errors.handler.external;

import com.demo.bbq.utils.errors.exceptions.ExternalServiceException;
import com.demo.bbq.utils.errors.handler.external.service.RestClientErrorService;
import com.demo.bbq.utils.properties.ConfigurationBaseProperties;
import java.util.List;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.web.client.HttpStatusCodeException;

public class ExternalClientErrorUtil {

  private ExternalClientErrorUtil() {}

  public static Throwable handleError(HttpStatusCodeException httpException,
                                      Class<?> errorWrapperClass,
                                      String serviceName,
                                      List<RestClientErrorService> serviceList,
                                      ConfigurationBaseProperties properties) {

    Pair<String, String> pairCodeAndMessage = selectService(errorWrapperClass, serviceList).getCodeAndMessage(httpException);
    String code = pairCodeAndMessage.getLeft();
    String message = pairCodeAndMessage.getRight();
    String selectedMessage = selectMessage(properties, code, message, serviceName);
    return new ExternalServiceException(code, selectedMessage, httpException.getStatusCode());
  }

  private static String selectMessage(ConfigurationBaseProperties properties,
                                      String errorCode,
                                      String errorMessage,
                                      String serviceName) {

    boolean showMessage = properties.isShowCustomMessages();
    return showMessage ? properties.getRestClients().get(serviceName).getErrors().get(errorCode) : errorMessage;
  }

  private static RestClientErrorService selectService(Class<?> errorWrapperClass,
                                                      List<RestClientErrorService> serviceList) {
    return serviceList
        .stream()
        .filter(service -> service.supports(errorWrapperClass))
        .findFirst()
        .orElseThrow();
  }

}
