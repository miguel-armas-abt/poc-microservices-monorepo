package com.demo.poc.commons.core.properties.logging;

import java.util.Map;
import java.util.Optional;

public interface LoggingTemplate {

  Map<String, Boolean> loggingType();
  Optional<ObfuscationTemplate> obfuscation();
}
