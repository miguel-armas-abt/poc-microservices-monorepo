package com.demo.bbq.commons.core.config;

import com.demo.bbq.commons.core.restclient.WebClientFactory;
import io.micrometer.observation.ObservationRegistry;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;

@Configuration
public class RestClientConfig {

  @Bean
  public WebClientFactory webClient(List<ExchangeFilterFunction> filters,
                                    ObservationRegistry observationRegistry) {
    return new WebClientFactory(filters, observationRegistry);
  }

}
