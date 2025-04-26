package com.demo.poc.commons.core.config;

import com.demo.poc.commons.core.errors.selector.RestClientErrorSelector;
import com.demo.poc.commons.core.restclient.error.RestClientErrorHandler;
import com.demo.poc.commons.core.restclient.error.RestClientErrorExtractor;
import com.demo.poc.commons.core.restclient.WebClientFactory;
import io.micrometer.observation.ObservationRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;

import java.util.List;

@Configuration
public class RestClientConfig {

  @Bean
  public WebClientFactory webClient(List<ExchangeFilterFunction> filters,
                                    ObservationRegistry observationRegistry) {
    return new WebClientFactory(filters, observationRegistry);
  }

  @Bean
  public RestClientErrorHandler restClientErrorHandler(List<RestClientErrorExtractor> restClientErrorExtractors,
                                                       RestClientErrorSelector restClientErrorSelector) {
    return new RestClientErrorHandler(restClientErrorExtractors, restClientErrorSelector);
  }
}
