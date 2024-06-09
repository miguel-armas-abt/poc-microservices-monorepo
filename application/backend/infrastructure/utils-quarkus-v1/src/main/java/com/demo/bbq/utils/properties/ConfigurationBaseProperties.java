package com.demo.bbq.utils.properties;

import com.demo.bbq.utils.properties.dto.ErrorMessage;
import com.demo.bbq.utils.properties.dto.RestClient;
import java.util.Map;
import java.util.Optional;

public interface ConfigurationBaseProperties {

  Optional<ErrorMessage> errorMessages();
  Map<String, RestClient> restClients();
}