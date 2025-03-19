package com.demo.bbq.commons.properties;

import com.demo.bbq.commons.properties.base.ConfigurationBaseProperties;
import com.demo.bbq.commons.properties.base.restclient.HeaderTemplate;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "configuration")
public class ApplicationProperties extends ConfigurationBaseProperties {

  private MenuInfo menuInfo;

  public String searchEndpoint(String serviceName) {
    return this.getRestClients().get(serviceName).getRequest().getEndpoint();
  }

  public HeaderTemplate searchHeaderTemplate(String serviceName) {
    return this.getRestClients().get(serviceName).getRequest().getHeaders();
  }

}
