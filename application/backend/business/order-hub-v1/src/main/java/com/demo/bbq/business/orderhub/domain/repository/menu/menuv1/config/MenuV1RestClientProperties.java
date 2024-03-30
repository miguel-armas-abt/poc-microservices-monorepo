package com.demo.bbq.business.orderhub.domain.repository.menu.menuv1.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@Component
@Getter
@Setter
public class MenuV1RestClientProperties {

  @Value("${application.http-client.menu.v1.base-url}")
  private String baseURL;
}