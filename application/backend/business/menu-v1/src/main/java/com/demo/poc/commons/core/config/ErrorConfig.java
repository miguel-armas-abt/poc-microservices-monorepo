package com.demo.poc.commons.core.config;

import com.demo.poc.commons.core.restclient.error.extractor.poc.DefaultErrorExtractor;
import com.demo.poc.commons.core.errors.selector.ResponseErrorSelector;
import com.demo.poc.commons.core.errors.selector.RestClientErrorSelector;
import com.demo.poc.commons.core.properties.ConfigurationBaseProperties;
import com.demo.poc.commons.core.serialization.JsonSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ErrorConfig {

  @Bean
  public DefaultErrorExtractor defaultErrorExtractor(JsonSerializer jsonSerializer) {
    return new DefaultErrorExtractor(jsonSerializer);
  }

  @Bean
  public ResponseErrorSelector responseErrorSelector(ConfigurationBaseProperties properties) {
    return new ResponseErrorSelector(properties);
  }

  @Bean
  public RestClientErrorSelector restClientErrorSelector(ConfigurationBaseProperties properties) {
    return new RestClientErrorSelector(properties);
  }
}
