package com.demo.bbq.commons.errors.handler.external;

import static com.demo.bbq.commons.errors.handler.external.ExternalErrorHandlerBase.selectCode;
import static com.demo.bbq.commons.errors.handler.external.ExternalErrorHandlerBase.selectMessage;
import static com.demo.bbq.commons.errors.handler.external.ExternalErrorHandlerBase.selectType;
import static com.demo.bbq.commons.errors.handler.external.ExternalErrorHandlerBase.selectHttpCode;
import static com.demo.bbq.commons.errors.handler.external.ExternalErrorHandlerBase.emptyResponse;
import static com.demo.bbq.commons.errors.handler.external.ExternalErrorHandlerBase.noSuchWrapper;

import com.demo.bbq.commons.errors.dto.ErrorDTO;
import com.demo.bbq.commons.errors.dto.ErrorType;
import com.demo.bbq.commons.errors.exceptions.ExternalServiceException;
import com.demo.bbq.commons.errors.exceptions.SystemException;
import com.demo.bbq.commons.errors.handler.external.strategy.ExternalErrorWrapper;
import com.demo.bbq.commons.errors.handler.external.strategy.RestClientErrorStrategy;
import com.demo.bbq.commons.properties.ConfigurationBaseProperties;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.HttpStatusCodeException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExternalErrorHandlerUtil {

  public static ExternalServiceException build(HttpStatusCodeException httpException,
                                               Class<? extends ExternalErrorWrapper> errorWrapperClass,
                                               String serviceName,
                                               List<RestClientErrorStrategy> serviceList,
                                               ConfigurationBaseProperties properties) {
    ErrorDTO defaultError = ErrorDTO.getDefaultError(properties);

    String jsonBody = httpException.getResponseBodyAsString();

    Pair<String, String> codeAndMessage = Strings.EMPTY.equals(jsonBody)
        ? emptyResponse(defaultError)
        : selectStrategy(errorWrapperClass, serviceList).getCodeAndMessage(jsonBody).orElseGet(() -> noSuchWrapper(defaultError));

    String selectedCode = selectCode(properties, codeAndMessage.getLeft(), serviceName);
    String selectedMessage = selectMessage(properties, selectedCode, codeAndMessage.getRight(), serviceName);
    ErrorType selectedErrorType = selectType(errorWrapperClass);
    HttpStatusCode selectedHttpStatus = HttpStatusCode.valueOf(selectHttpCode(properties, httpException.getStatusCode().value(), errorWrapperClass, codeAndMessage.getLeft(), serviceName));

    return new ExternalServiceException(selectedCode, selectedMessage, selectedErrorType, selectedHttpStatus);
  }

  private static RestClientErrorStrategy selectStrategy(Class<? extends ExternalErrorWrapper> errorWrapperClass,
                                                        List<RestClientErrorStrategy> serviceList) {
    return serviceList
        .stream()
        .filter(service -> service.supports(errorWrapperClass))
        .findFirst()
        .orElseThrow(() -> new SystemException("NoSuchExternalErrorStrategy"));
  }
}
