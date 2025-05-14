package com.demo.poc.commons.core.properties;

import com.demo.poc.commons.core.properties.logging.LoggingTemplate;
import com.demo.poc.commons.core.properties.restclient.RestClient;

import java.util.Map;
import java.util.Optional;

public interface ConfigurationBaseProperties {

  Optional<ProjectType> projectType();

  Optional<LoggingTemplate> logging();

  Map<String, String> errorMessages();

  Map<String, RestClient> restClients();

}