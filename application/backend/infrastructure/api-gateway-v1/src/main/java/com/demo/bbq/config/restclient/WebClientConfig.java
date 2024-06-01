package com.demo.bbq.config.restclient;

import com.demo.bbq.utils.restclient.webclient.WebClientFactory;
import com.demo.bbq.utils.tracing.logging.obfuscation.header.strategy.HeaderObfuscationMultipleStrategy;
import com.demo.bbq.utils.tracing.logging.obfuscation.header.strategy.HeaderObfuscationStandardStrategy;
import com.demo.bbq.utils.tracing.logging.obfuscation.header.strategy.HeaderObfuscationStrategy;
import io.micrometer.observation.ObservationRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Configuration
public class WebClientConfig {

  @Bean
  public HeaderObfuscationMultipleStrategy headerObfuscationMultipleStrategy(HeaderObfuscationStrategy strategy) {
    return new HeaderObfuscationMultipleStrategy(strategy);
  }

  @Bean
  public HeaderObfuscationStandardStrategy headerObfuscationStandardStrategy() {
    return new HeaderObfuscationStandardStrategy();
  }

  @Bean
  public WebClient webClient(List<ExchangeFilterFunction> filters,
                             ObservationRegistry observationRegistry) {
    return WebClientFactory.createWebClient(filters, observationRegistry);
  }

}

