package com.demo.bbq.application.properties;

import com.demo.bbq.utils.properties.ConfigurationBaseProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "configuration")
public class ServiceConfigurationProperties extends ConfigurationBaseProperties {

  public String searchEndpoint(String serviceName) {
    return this.getRestClients().get(serviceName).getRequest().getEndpoint();
  }

}
