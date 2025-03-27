package com.demo.poc.commons.core.config;

import com.demo.poc.commons.core.errors.external.strategy.RestClientErrorStrategy;
import com.demo.poc.commons.custom.properties.ApplicationProperties;
import com.demo.poc.commons.core.restclient.CustomRestTemplate;
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