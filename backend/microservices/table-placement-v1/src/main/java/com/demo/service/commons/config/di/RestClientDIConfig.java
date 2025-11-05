package com.demo.service.commons.config.di;

import com.demo.commons.errors.selector.RestClientErrorSelector;
import com.demo.commons.restclient.WebClientFactory;
import com.demo.commons.restclient.error.RestClientErrorExtractor;
import com.demo.commons.restclient.error.RestClientErrorHandler;
import io.micrometer.observation.ObservationRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;

import java.util.List;

@Configuration
public class RestClientDIConfig {

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
