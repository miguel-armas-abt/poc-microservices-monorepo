package com.demo.poc.commons.core.config;

import com.demo.poc.commons.core.errors.selector.ResponseErrorSelector;
import com.demo.poc.commons.custom.properties.ApplicationProperties;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

@ApplicationScoped
public class ErrorConfig {

  @Produces
  public ResponseErrorSelector responseErrorSelector(ApplicationProperties properties) {
    return new ResponseErrorSelector(properties);
  }

}
