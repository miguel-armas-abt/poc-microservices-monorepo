package com.demo.bbq.config.properties;

import com.demo.bbq.utils.properties.ConfigurationBaseProperties;
import com.demo.bbq.utils.properties.dto.HeaderTemplate;
import com.demo.bbq.utils.restclient.headers.HeadersBuilderUtil;
import jakarta.servlet.http.HttpServletRequest;
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

  public String searchEndpoint(String serviceName) {
    return this.getRestClients().get(serviceName).getRequest().getEndpoint();
  }

  public Map<String, String> searchHeaders(HttpServletRequest httpRequest, String serviceName) {
    HeaderTemplate headerTemplate = this.getRestClients().get(serviceName).getRequest().getHeaders();
    return HeadersBuilderUtil.buildHeaders(httpRequest, headerTemplate);
  }
}
