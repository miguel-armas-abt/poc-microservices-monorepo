package com.demo.bbq.entrypoint.calculator.repository.product;

import static com.demo.bbq.commons.toolkit.params.filler.HttpHeadersFiller.fillHeaders;

import com.demo.bbq.commons.properties.ApplicationProperties;
import com.demo.bbq.commons.restclient.webclient.WebClientFactory;
import com.demo.bbq.entrypoint.calculator.repository.product.wrapper.ProductResponseWrapper;
import com.demo.bbq.commons.errors.dto.ErrorDTO;
import com.demo.bbq.commons.errors.handler.external.ExternalErrorHandler;
import jakarta.annotation.PostConstruct;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class ProductRepository {

  private static final String SERVICE_NAME = "product-v1";

  private final ApplicationProperties properties;
  private final ExternalErrorHandler externalErrorHandler;
  private final WebClientFactory webClientFactory;

  private WebClient webClient;

  @PostConstruct
  public void init() {
    this.webClient = webClientFactory.createWebClient(properties.searchPerformance(SERVICE_NAME), SERVICE_NAME);
  }

  public Mono<ProductResponseWrapper> findByProductCode(Map<String, String> headers, String productCode) {
    return webClient.get()
        .uri(UriComponentsBuilder
            .fromUriString(properties.searchEndpoint(SERVICE_NAME).concat("{productCode}"))
            .buildAndExpand(productCode).toUriString())
        .headers(fillHeaders(properties.searchHeaderTemplate(SERVICE_NAME), headers))
        .retrieve()
        .onStatus(HttpStatusCode::isError, clientResponse -> externalErrorHandler.handleError(clientResponse, ErrorDTO.class, SERVICE_NAME))
        .toEntity(ProductResponseWrapper.class)
        .mapNotNull(HttpEntity::getBody);
  }
}

