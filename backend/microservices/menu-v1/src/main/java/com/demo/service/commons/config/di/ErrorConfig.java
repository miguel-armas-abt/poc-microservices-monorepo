package com.demo.service.commons.config.di;

import com.demo.commons.errors.selector.ResponseErrorSelector;
import com.demo.commons.properties.ConfigurationBaseProperties;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

@ApplicationScoped
public class ErrorConfig {

  @Produces
  public ResponseErrorSelector responseErrorSelector(ConfigurationBaseProperties properties) {
    return new ResponseErrorSelector(properties);
  }

}
