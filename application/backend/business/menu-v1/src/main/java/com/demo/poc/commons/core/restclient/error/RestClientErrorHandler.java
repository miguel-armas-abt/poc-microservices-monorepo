package com.demo.poc.commons.core.restclient.error;

import com.demo.poc.commons.core.errors.dto.ErrorDto;
import com.demo.poc.commons.core.errors.dto.ErrorType;
import com.demo.poc.commons.core.errors.exceptions.RestClientException;
import com.demo.poc.commons.core.errors.exceptions.NoSuchRestClientErrorExtractorException;
import com.demo.poc.commons.core.errors.selector.RestClientErrorSelector;
import com.demo.poc.commons.core.properties.ConfigurationBaseProperties;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.List;

import static com.demo.poc.commons.core.errors.selector.RestClientErrorSelector.selectType;

@RequiredArgsConstructor
public class RestClientErrorHandler {

  private final List<RestClientErrorExtractor> restClientErrorExtractors;
  private final RestClientErrorSelector restClientErrorSelector;
  private final ConfigurationBaseProperties properties;

  public RestClientException build(HttpStatusCodeException httpException,
                                   Class<?> errorWrapperClass,
                                   String serviceName) {

    ErrorDto defaultError = ErrorDto.getDefaultError(properties);

    String jsonBody = httpException.getResponseBodyAsString();

    Pair<String, String> codeAndMessage = Strings.EMPTY.equals(jsonBody)
        ? restClientErrorSelector.getDefaultResponse(defaultError, "Empty response")
        : selectExtractor(errorWrapperClass).getCodeAndMessage(jsonBody).orElseGet(() -> restClientErrorSelector.getDefaultResponse(defaultError, "No such external error wrapper"));

    String selectedCode = restClientErrorSelector.selectCode(codeAndMessage.getLeft(), serviceName);
    String selectedMessage = restClientErrorSelector.selectMessage(selectedCode, codeAndMessage.getRight(), serviceName);
    ErrorType selectedErrorType = selectType(errorWrapperClass);
    HttpStatusCode selectedHttpStatus = HttpStatusCode.valueOf(restClientErrorSelector.selectHttpCode(httpException.getStatusCode().value(), errorWrapperClass, codeAndMessage.getLeft(), serviceName));

    return new RestClientException(selectedCode, selectedMessage, selectedErrorType, selectedHttpStatus);
  }

  private RestClientErrorExtractor selectExtractor(Class<?> errorWrapperClass) {
    return restClientErrorExtractors
        .stream()
        .filter(extractor -> extractor.supports(errorWrapperClass))
        .findFirst()
        .orElseThrow(NoSuchRestClientErrorExtractorException::new);
  }
}
