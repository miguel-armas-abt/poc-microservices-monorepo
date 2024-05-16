package com.demo.bbq.config.restclient;

import com.demo.bbq.config.properties.ServiceConfigurationProperties;
import com.demo.bbq.utils.errors.handler.external.strategy.RestClientErrorStrategy;
import com.demo.bbq.utils.restclient.resttemplate.CustomRestTemplateUtil;
import com.demo.bbq.utils.restclient.resttemplate.dto.ExchangeRequestDTO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomRestTemplate {

  private final ServiceConfigurationProperties properties;
  private final List<RestClientErrorStrategy> strategies;

  public <I,O> O exchange(ExchangeRequestDTO<I, O> request, String serviceName) {
    return CustomRestTemplateUtil
        .exchange(request, serviceName, strategies, properties);
  }

}