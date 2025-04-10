package com.demo.poc.commons.core.config;

import com.demo.poc.commons.core.errors.selector.RestClientErrorSelector;
import com.demo.poc.commons.core.properties.ConfigurationBaseProperties;
import com.demo.poc.commons.core.restclient.error.RestClientErrorHandler;
import com.demo.poc.commons.core.restclient.error.RestClientErrorExtractor;
import com.demo.poc.commons.custom.properties.ApplicationProperties;
import com.demo.poc.commons.core.restclient.RestClientTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Configuration
public class RestClientConfig {

  @Bean
  public RestTemplate restTemplate(List<ClientHttpRequestInterceptor> requestInterceptors) {
    RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
    restTemplate.setInterceptors(requestInterceptors);
    return restTemplate;
  }

  @Bean
  public RestClientErrorHandler restClientErrorHandler(List<RestClientErrorExtractor> errorStrategies,
                                                       RestClientErrorSelector restClientErrorSelector,
                                                       ConfigurationBaseProperties properties) {
    return new RestClientErrorHandler(errorStrategies, restClientErrorSelector, properties);
  }

  @Bean
  public RestClientTemplate customRestTemplate(RestTemplate restTemplate,
                                               ApplicationProperties properties,
                                               RestClientErrorHandler restClientErrorHandler) {
    return new RestClientTemplate(restTemplate, properties, restClientErrorHandler);
  }
}