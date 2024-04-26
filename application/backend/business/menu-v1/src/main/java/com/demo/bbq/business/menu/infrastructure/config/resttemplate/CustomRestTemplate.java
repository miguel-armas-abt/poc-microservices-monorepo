package com.demo.bbq.business.menu.infrastructure.config.resttemplate;

import com.demo.bbq.business.menu.application.properties.ServiceConfigurationProperties;
import com.demo.bbq.utils.config.resttemplate.CustomRestTemplateUtil;
import com.demo.bbq.utils.config.resttemplate.dto.ExchangeRequestDTO;
import com.demo.bbq.utils.errors.handler.external.service.RestClientErrorService;
import java.util.List;
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