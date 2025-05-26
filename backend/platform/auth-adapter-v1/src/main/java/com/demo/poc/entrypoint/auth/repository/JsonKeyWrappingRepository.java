package com.demo.poc.entrypoint.auth.repository;

import com.auth0.jwk.Jwk;
import com.auth0.jwk.UrlJwkProvider;
import com.demo.poc.commons.custom.properties.ApplicationProperties;
import com.demo.poc.commons.custom.properties.KeycloakConnection;
import java.net.URL;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JsonKeyWrappingRepository {

  private final ApplicationProperties properties;

  @Cacheable(value = "jwkCache")
  public Jwk getJwk() throws Exception {
    KeycloakConnection keycloak = properties.getKeycloak();

    String jwkEndpoint = keycloak.getJwkEndpoint();
    URL url = new URL(jwkEndpoint);
    UrlJwkProvider urlJwkProvider = new UrlJwkProvider(url);
    return urlJwkProvider.get(keycloak.getCertId().trim());
  }

}
