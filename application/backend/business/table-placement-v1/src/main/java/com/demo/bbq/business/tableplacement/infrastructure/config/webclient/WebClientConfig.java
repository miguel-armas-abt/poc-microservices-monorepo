package com.demo.bbq.business.tableplacement.infrastructure.config.webclient;

import com.demo.bbq.business.tableplacement.infrastructure.config.logging.ExchangeRequestFilter;
import com.demo.bbq.business.tableplacement.infrastructure.config.logging.ExchangeResponseFilter;
import com.demo.bbq.utils.config.webclient.WebClientFactory;
import com.demo.bbq.utils.config.webclient.obfuscation.header.strategy.HeaderObfuscationMultipleStrategy;
import com.demo.bbq.utils.config.webclient.obfuscation.header.strategy.HeaderObfuscationStandardStrategy;
import com.demo.bbq.utils.config.webclient.obfuscation.header.strategy.HeaderObfuscationStrategy;
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
  public WebClient createWebClient(ExchangeRequestFilter exchangeRequestFilter,
                                   ExchangeResponseFilter exchangeResponseFilter) {
    return WebClientFactory.createWebClient(exchangeRequestFilter, exchangeResponseFilter);
  }

}
