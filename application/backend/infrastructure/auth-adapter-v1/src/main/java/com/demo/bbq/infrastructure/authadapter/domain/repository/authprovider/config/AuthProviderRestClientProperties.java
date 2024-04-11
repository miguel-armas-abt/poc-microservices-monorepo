package com.demo.bbq.infrastructure.authadapter.domain.repository.authprovider.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

@Getter
@RefreshScope
@ConfigurationProperties
@Configuration
public class AuthProviderRestClientProperties {

  @Value("${keycloak.base-url}")
  private String baseURL;

  @Value("${keycloak.client-id}")
  private String clientId;

  @Value("${keycloak.authorization-grant-type}")
  private String grantType;

  @Value("${keycloak.authorization-grant-type-refresh}")
  private String grantTypeRefresh;

  @Value("${keycloak.client-secret}")
  private String clientSecret;

  @Value("${keycloak.scope}")
  private String scope;
}
