package com.demo.bbq.commons.core.config;

import com.demo.bbq.commons.core.errors.external.strategy.RestClientErrorStrategy;
import com.demo.bbq.commons.custom.properties.ApplicationProperties;
import com.demo.bbq.commons.core.restclient.CustomRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;

import java.util.List;

@Configuration
public class RestClientConfig {

  @Bean
  public CustomRestTemplate customRestTemplate(ApplicationProperties properties,
                                               List<RestClientErrorStrategy> errorStrategies,
                                               List<ClientHttpRequestInterceptor> interceptors) {
    return new CustomRestTemplate(properties, errorStrategies, interceptors);
  }

}