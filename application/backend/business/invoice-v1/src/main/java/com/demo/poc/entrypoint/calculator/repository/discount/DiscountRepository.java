package com.demo.poc.entrypoint.calculator.repository.discount;

import com.demo.poc.commons.core.errors.dto.ErrorDTO;
import com.demo.poc.commons.core.errors.handler.external.ExternalErrorHandler;
import com.demo.poc.commons.custom.properties.ApplicationProperties;
import com.demo.poc.commons.core.restclient.WebClientFactory;
import com.demo.poc.entrypoint.calculator.repository.discount.wrapper.DiscountRequestWrapper;
import com.demo.poc.entrypoint.calculator.repository.discount.wrapper.DiscountResponseWrapper;
import jakarta.annotation.PostConstruct;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static com.demo.poc.commons.core.restclient.utils.HttpHeadersFiller.fillHeaders;

@Repository
@RequiredArgsConstructor
public class DiscountRepository {

  private static final String SERVICE_NAME = "rules-processor-v1";

  private final ApplicationProperties properties;
  private final ExternalErrorHandler externalErrorHandler;
  private final WebClientFactory webClientFactory;

  private WebClient webClient;

  @PostConstruct
  public void init() {
    this.webClient = webClientFactory.createWebClient(properties.searchPerformance(SERVICE_NAME), SERVICE_NAME);
  }

  public Mono<DiscountResponseWrapper> retrieveDiscount(Map<String, String> headers, DiscountRequestWrapper request) {
    return webClient.post()
        .uri(properties.searchEndpoint(SERVICE_NAME))
        .headers(fillHeaders(properties.searchHeaderTemplate(SERVICE_NAME), headers))
        .body(BodyInserters.fromValue(request))
        .retrieve()
        .onStatus(HttpStatusCode::isError, clientResponse -> externalErrorHandler.handleError(clientResponse, ErrorDTO.class, SERVICE_NAME))
        .toEntity(DiscountResponseWrapper.class)
        .mapNotNull(HttpEntity::getBody);

  }
}
