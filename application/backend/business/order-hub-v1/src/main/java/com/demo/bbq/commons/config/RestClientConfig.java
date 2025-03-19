package com.demo.bbq.commons.config;

import com.demo.bbq.commons.restclient.WebClientFactory;
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

}
