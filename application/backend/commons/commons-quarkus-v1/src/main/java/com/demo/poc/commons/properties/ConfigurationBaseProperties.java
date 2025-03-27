package com.demo.poc.commons.properties;

import com.demo.poc.commons.properties.dto.ErrorMessage;
import com.demo.poc.commons.properties.dto.RestClient;
import java.util.Map;
import java.util.Optional;

public interface ConfigurationBaseProperties {

  Optional<ErrorMessage> errorMessages();
  Map<String, RestClient> restClients();
}