package com.demo.bbq.config.restclient;

import com.demo.bbq.config.tracing.logging.ClientRequestLogging;
import com.demo.bbq.config.tracing.logging.ClientResponseLogging;
import com.demo.bbq.utils.restclient.webclient.WebClientFactory;
import com.demo.bbq.utils.tracing.logging.obfuscation.header.strategy.HeaderObfuscationMultipleStrategy;
import com.demo.bbq.utils.tracing.logging.obfuscation.header.strategy.HeaderObfuscationStandardStrategy;
import com.demo.bbq.utils.tracing.logging.obfuscation.header.strategy.HeaderObfuscationStrategy;
import io.micrometer.observation.ObservationRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

  @Bean
  public HeaderObfuscationMultipleStrategy createHeaderObfuscationMultipleStrategy(HeaderObfuscationStrategy strategy) {
    return new HeaderObfuscationMultipleStrategy(strategy);
  }

  @Bean
  public HeaderObfuscationStandardStrategy createHeaderObfuscationStandardStrategy() {
    return new HeaderObfuscationStandardStrategy();
  }

  @Bean
  public WebClient createWebClient(ClientRequestLogging clientRequestLogging,
                                   ClientResponseLogging clientResponseLogging,
                                   ObservationRegistry observationRegistry) {
    return WebClientFactory.createWebClient(clientRequestLogging, clientResponseLogging, observationRegistry);
  }

}
