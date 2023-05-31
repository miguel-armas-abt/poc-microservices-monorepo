package com.demo.bbq.infrastructure.authadapter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

  @Bean
  public RestTemplate getRestTemplate() {
    HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
    httpRequestFactory.setConnectionRequestTimeout(15000);
    httpRequestFactory.setConnectTimeout(15000);
    httpRequestFactory.setReadTimeout(15000);
    return new RestTemplate(httpRequestFactory);
  }
}
