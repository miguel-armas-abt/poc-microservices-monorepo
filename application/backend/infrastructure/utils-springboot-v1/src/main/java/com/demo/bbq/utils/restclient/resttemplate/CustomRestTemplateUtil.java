package com.demo.bbq.utils.restclient.resttemplate;

import java.util.List;
import com.demo.bbq.utils.restclient.resttemplate.dto.ExchangeRequestDTO;
import com.demo.bbq.utils.errors.matcher.ExternalErrorMatcherUtil;
import com.demo.bbq.utils.errors.external.RestClientErrorService;
import com.demo.bbq.utils.properties.ConfigurationBaseProperties;
import org.springframework.web.client.HttpStatusCodeException;

public class CustomRestTemplateUtil {

  public static <I,O> O exchange(ExchangeRequestDTO<I> request, String serviceName,
                          List<RestClientErrorService> errorServices, ConfigurationBaseProperties properties) {
    try {
      return (O) RestTemplateFactory
          .createRestTemplate()
          .exchange(request.getUrl(), request.getHttpMethod(), request.getHttpEntity(), request.getResponseClass(), request.getUriVariables())
          .getBody();

    } catch (HttpStatusCodeException httpException) {
      throw ExternalErrorMatcherUtil.build(httpException, request.getErrorWrapperClass(), serviceName, errorServices, properties);
    }
  }

}
