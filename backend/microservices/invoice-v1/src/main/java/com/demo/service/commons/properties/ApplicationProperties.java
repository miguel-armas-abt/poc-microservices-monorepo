package com.demo.service.commons.properties;

import com.demo.commons.properties.ConfigurationBaseProperties;
import com.demo.commons.properties.restclient.HeaderTemplate;
import com.demo.service.commons.properties.custom.CustomTemplate;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "configuration")
public class ApplicationProperties extends ConfigurationBaseProperties {

  private CustomTemplate custom;

  public String searchEndpoint(String serviceName) {
    return this.getRestClients().get(serviceName).getRequest().getEndpoint();
  }

  public HeaderTemplate searchHeaderTemplate(String serviceName) {
    return this.getRestClients().get(serviceName).getRequest().getHeaders();
  }

}
