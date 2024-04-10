package com.demo.bbq.infrastructure.apigateway.domain.repository.authadapter.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@Component
@Getter
@Setter
public class AuthAdapterRestClientProperties {

  @Value("${application.http-client.auth-adapter.base-url}")
  private String baseURL;
}
