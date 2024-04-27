package com.demo.bbq.config.resttemplate;

import com.demo.bbq.application.properties.ServiceConfigurationProperties;
import java.util.List;

import com.demo.bbq.utils.errors.external.RestClientErrorService;
import com.demo.bbq.utils.restclient.resttemplate.CustomRestTemplateUtil;
import com.demo.bbq.utils.restclient.resttemplate.dto.ExchangeRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomRestTemplate {

  private final ServiceConfigurationProperties properties;
  private final List<RestClientErrorService> errorServices;

  public <I,O> O exchange(ExchangeRequestDTO<I> request, String serviceName) {
    return CustomRestTemplateUtil
        .exchange(request, serviceName, errorServices, properties);
  }

}