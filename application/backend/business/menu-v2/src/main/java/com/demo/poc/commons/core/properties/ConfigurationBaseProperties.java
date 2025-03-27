package com.demo.poc.commons.core.properties;

import com.demo.poc.commons.core.properties.obfuscation.ObfuscationTemplate;
import com.demo.poc.commons.core.properties.restclient.RestClient;

import java.util.Map;
import java.util.Optional;

public interface ConfigurationBaseProperties {

  Optional<String> projectType();

  Map<String, String> errorMessages();

  Map<String, RestClient> restClients();

  Optional<ObfuscationTemplate> obfuscation();
}