package com.demo.bbq.commons.restclient;

import com.demo.bbq.commons.properties.ApplicationProperties;
import com.demo.bbq.commons.errors.handler.external.strategy.RestClientErrorStrategy;
import com.demo.bbq.commons.restclient.resttemplate.CustomRestTemplate;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;

@Configuration
public class RestTemplateConfig {

  @Bean
  public CustomRestTemplate customRestTemplate(ApplicationProperties properties,
                                               List<RestClientErrorStrategy> errorStrategies,
                                               List<ClientHttpRequestInterceptor> interceptors) {
    return new CustomRestTemplate(properties, errorStrategies, interceptors);
  }

}