package com.demo.bbq.utils.properties;

import com.demo.bbq.utils.properties.dto.ErrorMessage;
import com.demo.bbq.utils.properties.dto.RestClient;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class ConfigurationBaseProperties {

  protected Map<String, RestClient> restClients;

  protected ErrorMessage errorMessages;

  protected Map<String, String> variables;
}
