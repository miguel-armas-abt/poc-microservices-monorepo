package com.demo.bbq.commons.config;

import com.demo.bbq.commons.properties.ApplicationProperties;
import com.demo.bbq.commons.errors.handler.external.ExternalErrorHandler;
import com.demo.bbq.commons.errors.handler.external.strategy.DefaultErrorStrategy;
import com.demo.bbq.commons.errors.handler.external.strategy.RestClientErrorStrategy;
import com.demo.bbq.commons.errors.handler.response.ResponseErrorHandler;
import com.demo.bbq.commons.toolkit.serialization.ByteSerializer;
import com.demo.bbq.commons.toolkit.serialization.JsonSerializer;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ErrorHandlerConfig {

  @Bean
  public DefaultErrorStrategy defaultErrorStrategy(JsonSerializer jsonSerializer) {
    return new DefaultErrorStrategy(jsonSerializer);
  }

  @Bean
  public ExternalErrorHandler externalErrorHandler(List<RestClientErrorStrategy> services,
                                                   ApplicationProperties properties) {
    return new ExternalErrorHandler(services, properties);
  }

  @Bean
  public ResponseErrorHandler responseErrorHandler(ByteSerializer byteSerializer) {
    return new ResponseErrorHandler(byteSerializer);
  }
}

