package com.demo.bbq.config.tracing.logging;

import com.demo.bbq.application.properties.ServiceConfigurationProperties;
import com.demo.bbq.utils.tracing.logging.RestClientRequestLogger;
import com.demo.bbq.utils.tracing.logging.RestClientResponseLogger;
import com.demo.bbq.utils.tracing.logging.RestServerLogger;
import com.demo.bbq.utils.tracing.logging.obfuscation.header.strategy.HeaderObfuscationStrategy;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.server.WebFilter;

@Configuration
public class LoggingConfig {

  @Bean
  public WebFilter restServerLogger(ServiceConfigurationProperties properties,
                                    List<HeaderObfuscationStrategy> headerObfuscationStrategies) {
    return new RestServerLogger(properties, headerObfuscationStrategies);
  }

  @Bean
  public ExchangeFilterFunction restClientRequestLogger(ServiceConfigurationProperties properties,
                                                        List<HeaderObfuscationStrategy> headerObfuscationStrategies) {
    return new RestClientRequestLogger(properties, headerObfuscationStrategies);
  }

  @Bean
  public ExchangeFilterFunction restClientResponseLogger(ServiceConfigurationProperties properties,
                                                         List<HeaderObfuscationStrategy> headerObfuscationStrategies) {
    return new RestClientResponseLogger(properties, headerObfuscationStrategies);
  }

}
