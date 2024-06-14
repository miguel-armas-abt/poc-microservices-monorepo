package com.demo.bbq.config.restclient;

import com.demo.bbq.commons.restclient.webclient.WebClientFactory;
import io.micrometer.observation.ObservationRegistry;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

  @Bean
  public WebClient webClient(List<ExchangeFilterFunction> filters,
                             ObservationRegistry observationRegistry) {
    return WebClientFactory.createWebClient(filters, observationRegistry);
  }

}

