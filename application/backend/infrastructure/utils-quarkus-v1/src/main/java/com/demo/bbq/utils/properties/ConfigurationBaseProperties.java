package com.demo.bbq.utils.properties;

import com.demo.bbq.utils.properties.dto.ErrorMessage;
import java.util.Optional;

public interface ConfigurationBaseProperties {

  Optional<ErrorMessage> errorMessages();

}