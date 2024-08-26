package com.demo.bbq.commons.properties;

import com.demo.bbq.commons.errors.exceptions.SystemException;
import com.demo.bbq.commons.properties.dto.messages.ErrorMessage;
import com.demo.bbq.commons.properties.dto.obfuscation.ObfuscationTemplate;
import com.demo.bbq.commons.properties.dto.restclient.HeaderTemplate;
import com.demo.bbq.commons.properties.dto.restclient.PerformanceTemplate;
import com.demo.bbq.commons.properties.dto.restclient.RestClient;
import java.util.Map;
import java.util.Optional;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class ConfigurationBaseProperties {

  protected ErrorMessage errorMessages;

  protected Map<String, RestClient> restClients;

  protected ObfuscationTemplate obfuscation;

  public PerformanceTemplate searchPerformance(String serviceName) {
    return searchRestClient(serviceName).getPerformance();
  }

  public String searchEndpoint(String serviceName) {
    return searchRestClient(serviceName).getRequest().getEndpoint();
  }

  public Map<String, String> searchFormData(String serviceName) {
    return searchRestClient(serviceName).getRequest().getFormData();
  }

  public HeaderTemplate searchHeaders(String serviceName) {
    return searchRestClient(serviceName).getRequest().getHeaders();
  }

  private RestClient searchRestClient(String serviceName) {
    return Optional.ofNullable(restClients.get(serviceName))
        .orElseThrow(() -> new SystemException("NoSuchRestClient"));
  }
}