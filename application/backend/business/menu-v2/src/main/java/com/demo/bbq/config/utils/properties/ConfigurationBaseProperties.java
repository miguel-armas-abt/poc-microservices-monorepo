package com.demo.bbq.config.utils.properties;

import java.util.Map;
import javax.enterprise.context.ApplicationScoped;
import lombok.Getter;
import lombok.Setter;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@Getter
@Setter
@ApplicationScoped
public class ConfigurationBaseProperties {

  @ConfigProperty(name = "configuration.error-messages.enabled")
  boolean errorMessagesEnabled;

  @ConfigProperty(name = "configuration.error-messages.messages")
  Map<String, String> errorMessages;

}