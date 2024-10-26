package com.java.buddies.properties;

import java.io.InputStream;

import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;
import org.yaml.snakeyaml.Yaml;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class PropertiesReader {

  private final ApplicationProperties properties;

  public PropertiesReader() {
    InputStream inputStream = PropertiesReader.class.getClassLoader().getResourceAsStream("application.yaml");
    this.properties = new Yaml().loadAs(inputStream, ApplicationProperties.class);
  }

  public ApplicationProperties get() {
    return this.properties;
  }

}
