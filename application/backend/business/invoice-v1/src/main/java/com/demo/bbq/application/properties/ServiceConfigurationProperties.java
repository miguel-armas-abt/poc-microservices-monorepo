package com.demo.bbq.application.properties;

import com.demo.bbq.application.rules.properties.RuleInfo;
import com.demo.bbq.utils.properties.ConfigurationBaseProperties;
import com.demo.bbq.utils.properties.dto.HeaderTemplate;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "configuration")
public class ServiceConfigurationProperties extends ConfigurationBaseProperties {

  private Map<String, RuleInfo> rules;

  public String searchEndpoint(String serviceName) {
    return this.getRestClients().get(serviceName).getRequest().getEndpoint();
  }

  public HeaderTemplate searchHeaderTemplate(String serviceName) {
    return this.getRestClients().get(serviceName).getRequest().getHeaders();
  }

}
