package com.demo.bbq.commons.config;

import com.demo.bbq.commons.errors.handler.response.ResponseErrorHandler;
import com.demo.bbq.commons.toolkit.serialize.ByteSerializer;
import com.demo.bbq.commons.toolkit.serialize.JsonSerializer;
import com.demo.bbq.commons.properties.ApplicationProperties;
import com.demo.bbq.commons.errors.handler.external.ExternalErrorHandler;
import com.demo.bbq.commons.errors.handler.external.strategy.DefaultErrorStrategy;
import com.demo.bbq.commons.errors.handler.external.strategy.RestClientErrorStrategy;
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