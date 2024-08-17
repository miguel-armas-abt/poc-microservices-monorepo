package com.demo.bbq.commons.restclient.resttemplate;

import com.demo.bbq.commons.errors.handler.external.strategy.RestClientErrorStrategy;
import com.demo.bbq.commons.properties.dto.restclient.HeaderTemplate;
import com.demo.bbq.commons.restclient.resttemplate.dto.ExchangeRequestDTO;
import com.demo.bbq.commons.errors.handler.external.ExternalErrorHandlerUtil;
import com.demo.bbq.commons.properties.ConfigurationBaseProperties;
import com.demo.bbq.commons.restclient.headers.HeadersBuilderUtil;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.HttpStatusCodeException;

@RequiredArgsConstructor
public class CustomRestTemplate {

  private final ConfigurationBaseProperties properties;
  private final List<RestClientErrorStrategy> errorStrategies;
  private final List<ClientHttpRequestInterceptor> interceptors;

  public <I,O> O exchange(ExchangeRequestDTO<I,O> request, String serviceName) {
    try {
      return RestTemplateFactory
          .createRestTemplate(interceptors)
          .exchange(request.getUrl(),
              request.getHttpMethod(),
              buildHttpEntity(request, properties.getRestClients().get(serviceName).getRequest().getHeaders()),
              request.getResponseClass(),
              request.getUriVariables())
          .getBody();

    } catch (HttpStatusCodeException httpException) {
      throw ExternalErrorHandlerUtil.build(httpException, request.getErrorWrapperClass(), serviceName, errorStrategies, properties);
    }
  }

  private static <I,O> HttpEntity<I> buildHttpEntity(ExchangeRequestDTO<I,O> request,
                                                     HeaderTemplate headerTemplate) {
    HttpHeaders headers = HeadersBuilderUtil.buildHeaders(request.getHeaders(), headerTemplate);
    headers.setContentType(MediaType.APPLICATION_JSON);
    return new HttpEntity<>(request.getRequestBody(), headers);
  }
}
