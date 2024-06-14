package com.demo.bbq.commons.properties;

import com.demo.bbq.commons.properties.dto.ErrorMessage;
import com.demo.bbq.commons.properties.dto.ObfuscationTemplate;
import com.demo.bbq.commons.properties.dto.RestClient;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class ConfigurationBaseProperties {

  protected ErrorMessage errorMessages;

  protected Map<String, RestClient> restClients;

  protected ObfuscationTemplate obfuscation;
}