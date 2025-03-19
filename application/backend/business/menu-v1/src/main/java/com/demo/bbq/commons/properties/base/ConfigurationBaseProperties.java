package com.demo.bbq.commons.properties.base;

import com.demo.bbq.commons.errors.exceptions.SystemException;
import com.demo.bbq.commons.properties.base.cache.CacheTemplate;
import com.demo.bbq.commons.properties.base.messages.ErrorMessage;
import com.demo.bbq.commons.properties.base.obfuscation.ObfuscationTemplate;
import com.demo.bbq.commons.properties.base.restclient.HeaderTemplate;
import com.demo.bbq.commons.properties.base.restclient.PerformanceTemplate;
import com.demo.bbq.commons.properties.base.restclient.RestClient;
import com.demo.bbq.commons.logging.enums.LoggingType;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.Optional;

@Getter
@Setter
public abstract class ConfigurationBaseProperties {

  protected ProjectType projectType;

  protected Map<String, Boolean> enabledLoggers;

  protected ErrorMessage errorMessages;

  protected Map<String, RestClient> restClients;

  protected Map<String, CacheTemplate> cache;

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

  public boolean isLoggerPresent(LoggingType loggingType) {
    return Optional.ofNullable(this.getEnabledLoggers())
        .filter(enabledLoggers -> enabledLoggers.containsKey(loggingType.getCode()))
        .map(enabledLoggers -> enabledLoggers.get(loggingType.getCode()))
        .orElseGet(() -> Boolean.FALSE);
  }
}