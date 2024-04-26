package com.demo.bbq.business.menu.infrastructure.config.resttemplate;

import com.demo.bbq.utils.config.resttemplate.RestTemplateFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

  @Bean
  public RestTemplate restTemplate() {
    return RestTemplateFactory.createRestTemplate();
  }

}