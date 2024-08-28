package com.demo.bbq.entrypoint.calculator.repository.discount;

import static com.demo.bbq.commons.toolkit.params.filler.HttpHeadersFiller.fillHeaders;

import com.demo.bbq.commons.errors.dto.ErrorDTO;
import com.demo.bbq.commons.errors.handler.external.ExternalErrorHandler;
import com.demo.bbq.commons.properties.ApplicationProperties;
import com.demo.bbq.commons.restclient.webclient.WebClientFactory;
import com.demo.bbq.entrypoint.calculator.repository.discount.wrapper.DiscountRequestWrapper;
import com.demo.bbq.entrypoint.calculator.repository.discount.wrapper.DiscountResponseWrapper;
import jakarta.annotation.PostConstruct;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

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
