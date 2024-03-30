package com.demo.bbq.business.orderhub.domain.repository.menu.menuv2.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@Component
@Getter
@Setter
public class MenuV2RestClientProperties {

  @Value("${application.http-client.menu.v2.base-url}")
  private String baseURL;
}
