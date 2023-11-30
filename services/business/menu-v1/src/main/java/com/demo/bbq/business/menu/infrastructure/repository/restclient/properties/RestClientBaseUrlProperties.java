package com.demo.bbq.business.menu.infrastructure.repository.restclient.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@Component
@Getter
@Setter
public class RestClientBaseUrlProperties {

  @Value("${application.http-client.product.base-url}")
  private String productBaseUrl;

}
