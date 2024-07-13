package com.demo.bbq.commons.properties;

import com.demo.bbq.commons.properties.dto.messages.ErrorMessage;
import com.demo.bbq.commons.properties.dto.obfuscation.ObfuscationTemplate;
import com.demo.bbq.commons.properties.dto.restclient.RestClient;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class ConfigurationBaseProperties {

  protected ErrorMessage errorMessages;

  protected Map<String, RestClient> restClients;

  protected ObfuscationTemplate obfuscation;

  protected Map<String, String> cryptography;
}