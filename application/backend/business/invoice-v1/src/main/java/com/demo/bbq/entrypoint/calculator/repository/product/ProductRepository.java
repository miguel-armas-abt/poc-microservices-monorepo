package com.demo.bbq.entrypoint.calculator.repository.product;

import com.demo.bbq.commons.custom.properties.ApplicationProperties;
import com.demo.bbq.commons.core.restclient.WebClientFactory;
import com.demo.bbq.entrypoint.calculator.repository.product.wrapper.ProductResponseWrapper;
import com.demo.bbq.commons.core.errors.dto.ErrorDTO;
import com.demo.bbq.commons.core.errors.handler.external.ExternalErrorHandler;
import jakarta.annotation.PostConstruct;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import static com.demo.bbq.commons.core.restclient.utils.HttpHeadersFiller.fillHeaders;

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

