package com.demo.bbq.utils.restclient.resttemplate;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Slf4j
public class RestTemplateFactory {

  public static RestTemplate createRestTemplate() {
    return new RestTemplate(new HttpComponentsClientHttpRequestFactory());
  }

}
