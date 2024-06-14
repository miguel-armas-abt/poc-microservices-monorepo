package com.demo.bbq.commons.properties;

import com.demo.bbq.commons.properties.dto.ErrorMessage;
import com.demo.bbq.commons.properties.dto.RestClient;
import java.util.Map;
import java.util.Optional;

public interface ConfigurationBaseProperties {

  Optional<ErrorMessage> errorMessages();
  Map<String, RestClient> restClients();
}