package com.demo.bbq.repository.authprovider;

import static com.demo.bbq.repository.authprovider.config.AuthProviderRestClientConfig.SERVICE_NAME;

import com.auth0.jwk.Jwk;
import com.auth0.jwk.UrlJwkProvider;
import com.demo.bbq.application.properties.ServiceConfigurationProperties;
import java.net.URL;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JsonWebTokenConnector {

  private final ServiceConfigurationProperties properties;

  @Cacheable(value = "jwkCache")
  public Jwk getJwk() throws Exception {
    URL url = new URL(searchVariable("jwk-set-uri"));
    UrlJwkProvider urlJwkProvider = new UrlJwkProvider(url);
    Jwk get = urlJwkProvider.get(searchVariable("certs-id").trim());
    return get;
  }

  private String searchVariable(String variableName) {
    return properties.searchVariables(SERVICE_NAME).get(variableName);
  }

}
