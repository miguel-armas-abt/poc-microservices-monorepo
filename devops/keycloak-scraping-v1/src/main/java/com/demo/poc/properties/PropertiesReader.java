package com.demo.poc.properties;

import com.demo.poc.utils.YamlReader;
import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class PropertiesReader {

  private final ApplicationProperties properties;

  public PropertiesReader() {
    this.properties = YamlReader.read("application.yaml", ApplicationProperties.class);
  }

  public ApplicationProperties get() {
    return this.properties;
  }

}
