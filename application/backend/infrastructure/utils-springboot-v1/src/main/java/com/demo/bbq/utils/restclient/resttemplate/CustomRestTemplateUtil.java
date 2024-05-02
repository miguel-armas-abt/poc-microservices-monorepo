package com.demo.bbq.utils.restclient.resttemplate;

import com.demo.bbq.utils.properties.dto.HeaderTemplate;
import com.demo.bbq.utils.restclient.resttemplate.dto.ExchangeRequestDTO;
import com.demo.bbq.utils.errors.matcher.ExternalErrorMatcherUtil;
import com.demo.bbq.utils.errors.external.RestClientErrorService;
import com.demo.bbq.utils.properties.ConfigurationBaseProperties;
import com.demo.bbq.utils.restclient.resttemplate.headers.HeadersBuilderUtil;
import java.util.List;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpStatusCodeException;

public class CustomRestTemplateUtil {

  public static <I,O> O exchange(ExchangeRequestDTO<I,O> request,
                                 String serviceName,
                                 List<RestClientErrorService> errorServices,
                                 ConfigurationBaseProperties properties) {
    try {
      return RestTemplateFactory
          .createRestTemplate()
          .exchange(request.getUrl(),
              request.getHttpMethod(),
              buildHttpEntity(request, properties.getRestClients().get(serviceName).getRequest().getHeaders()),
              request.getResponseClass(),
              request.getUriVariables())
          .getBody();

    } catch (HttpStatusCodeException httpException) {
      throw ExternalErrorMatcherUtil.build(httpException, request.getErrorWrapperClass(), serviceName, errorServices, properties);
    }
  }

  private static <I,O> HttpEntity<I> buildHttpEntity(ExchangeRequestDTO<I,O> request,
                                                     HeaderTemplate headerTemplate) {
    HttpHeaders headers = HeadersBuilderUtil.buildHeaders(request.getHttpServletRequest(), headerTemplate);
    headers.setContentType(MediaType.APPLICATION_JSON);
    return new HttpEntity<>(request.getRequestBody(), headers);
  }
}
