package com.demo.service.entrypoint.calculator.repository.product;

import com.demo.commons.properties.restclient.RestClient;
import com.demo.commons.restclient.error.RestClientErrorHandler;
import com.demo.service.commons.properties.ApplicationProperties;
import com.demo.commons.restclient.WebClientFactory;
import com.demo.service.entrypoint.calculator.repository.product.wrapper.ProductResponseWrapper;
import com.demo.commons.errors.dto.ErrorDto;
import java.util.Map;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import static com.demo.commons.restclient.utils.HttpHeadersFiller.fillHeaders;

@Repository
public class ProductRepository {

  private static final String SERVICE_NAME = "product-v1";

  private final RestClientErrorHandler errorHandler;
  private final WebClient webClient;
  private final RestClient restClient;

  public ProductRepository(ApplicationProperties properties,
                           RestClientErrorHandler errorHandler,
                           WebClientFactory webClientFactory) {
    this.errorHandler = errorHandler;

    this.restClient = properties.searchRestClient(SERVICE_NAME);
    this.webClient = webClientFactory.createWebClient(restClient.getPerformance(), SERVICE_NAME);
  }

  public Mono<ProductResponseWrapper> findByProductCode(Map<String, String> headers, String productCode) {
    return webClient.get()
        .uri(UriComponentsBuilder
            .fromUriString(restClient.getRequest().getEndpoint().concat("{productCode}"))
            .buildAndExpand(productCode).toUriString())
        .headers(fillHeaders(restClient.getRequest().getHeaders(), headers))
        .retrieve()
        .onStatus(HttpStatusCode::isError, clientResponse -> errorHandler.handleError(clientResponse, ErrorDto.class, SERVICE_NAME))
        .toEntity(ProductResponseWrapper.class)
        .mapNotNull(HttpEntity::getBody);
  }
}

