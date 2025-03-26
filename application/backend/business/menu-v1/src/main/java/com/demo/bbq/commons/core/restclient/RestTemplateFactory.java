package com.demo.bbq.commons.core.restclient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
public class RestTemplateFactory {

  public static RestTemplate createRestTemplate(List<ClientHttpRequestInterceptor> requestInterceptors) {
    RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
    restTemplate.setInterceptors(requestInterceptors);
    return restTemplate;
  }

}
