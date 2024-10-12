package com.demo.bbq.commons.properties;

import com.demo.bbq.commons.errors.exceptions.SystemException;
import com.demo.bbq.commons.properties.dto.ProjectType;
import com.demo.bbq.commons.properties.dto.cache.CacheTemplate;
import com.demo.bbq.commons.properties.dto.messages.ErrorMessage;
import com.demo.bbq.commons.properties.dto.obfuscation.ObfuscationTemplate;
import com.demo.bbq.commons.properties.dto.restclient.HeaderTemplate;
import com.demo.bbq.commons.properties.dto.restclient.PerformanceTemplate;
import com.demo.bbq.commons.properties.dto.restclient.RestClient;
import com.demo.bbq.commons.tracing.logging.enums.LoggerType;
import java.util.Map;
import java.util.Optional;
import lombok.Getter;
import lombok.Setter;

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

  public boolean isLoggerPresent(LoggerType loggerType) {
    return Optional.ofNullable(this.getEnabledLoggers())
        .filter(enabledLoggers -> enabledLoggers.containsKey(loggerType.getCode()))
        .map(enabledLoggers -> enabledLoggers.get(loggerType.getCode()))
        .orElseGet(() -> Boolean.FALSE);
  }
}