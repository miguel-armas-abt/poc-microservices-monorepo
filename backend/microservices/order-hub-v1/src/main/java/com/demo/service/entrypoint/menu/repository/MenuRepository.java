package com.demo.service.entrypoint.menu.repository;

import com.demo.commons.errors.dto.ErrorDto;
import com.demo.commons.properties.restclient.RestClient;
import com.demo.commons.restclient.StreamingTransformer;
import com.demo.commons.restclient.WebClientFactory;
import com.demo.commons.restclient.error.RestClientErrorHandler;
import com.demo.commons.restclient.utils.HttpHeadersFiller;
import com.demo.service.commons.properties.ApplicationProperties;
import com.demo.service.entrypoint.menu.repository.wrapper.response.MenuOptionResponseWrapper;
import com.demo.service.entrypoint.menu.repository.wrapper.request.MenuOptionSaveRequestWrapper;
import java.util.Map;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class MenuRepository {

  private static final String SERVICE_NAME = "menu-v1";

  private final RestClientErrorHandler errorHandler;
  private final WebClient webClient;
  private final RestClient restClient;

  public MenuRepository(ApplicationProperties properties,
                        RestClientErrorHandler errorHandler,
                        WebClientFactory webClientFactory) {
    this.errorHandler = errorHandler;

    this.restClient = properties.searchRestClient(SERVICE_NAME);
    this.webClient = webClientFactory.createWebClient(restClient.getPerformance(), SERVICE_NAME);
  }

  public Mono<MenuOptionResponseWrapper> findByProductCode(Map<String, String> headers, String productCode) {
    return webClient.get()
        .uri(UriComponentsBuilder
            .fromUriString(restClient.getRequest().getEndpoint().concat("menu-options/{productCode}"))
            .buildAndExpand(productCode).toUriString())
        .headers(HttpHeadersFiller.fillHeaders(restClient.getRequest().getHeaders(), headers))
        .retrieve()
        .onStatus(HttpStatusCode::isError, this::handleError)
        .toEntity(MenuOptionResponseWrapper.class)
        .mapNotNull(HttpEntity::getBody);
  }

  public Flux<MenuOptionResponseWrapper> findByCategory(Map<String, String> headers, String category) {
    return webClient.get()
        .uri(UriComponentsBuilder
            .fromUriString(restClient.getRequest().getEndpoint().concat("menu-options"))
            .queryParam("category", category).toUriString())
        .accept(MediaType.APPLICATION_NDJSON)
        .headers(HttpHeadersFiller.fillHeaders(restClient.getRequest().getHeaders(), headers))
        .retrieve()
        .onStatus(HttpStatusCode::isError, this::handleError)
        .bodyToFlux(String.class)
        .flatMap(StreamingTransformer.of(MenuOptionResponseWrapper.class));
  }

  public Mono<Void> save(Map<String, String> headers, MenuOptionSaveRequestWrapper menuOption) {
    return webClient.post()
        .uri(UriComponentsBuilder
            .fromUriString(restClient.getRequest().getEndpoint().concat("menu-options")).toUriString())
        .contentType(MediaType.APPLICATION_JSON)
        .headers(HttpHeadersFiller.fillHeaders(restClient.getRequest().getHeaders(), headers))
        .body(BodyInserters.fromValue(menuOption))
        .retrieve()
        .onStatus(HttpStatusCode::isError, this::handleError)
        .toEntity(Void.class)
        .mapNotNull(HttpEntity::getBody);
  }

  public Mono<Void> update(Map<String, String> headers, String productCode, MenuOptionSaveRequestWrapper menuOption) {
    return webClient.post()
        .uri(UriComponentsBuilder
            .fromUriString(restClient.getRequest().getEndpoint().concat("menu-options/{productCode}"))
            .buildAndExpand(productCode).toUriString())
        .contentType(MediaType.APPLICATION_JSON)
        .headers(HttpHeadersFiller.fillHeaders(restClient.getRequest().getHeaders(), headers))
        .body(BodyInserters.fromValue(menuOption))
        .retrieve()
        .onStatus(HttpStatusCode::isError, this::handleError)
        .toEntity(Void.class)
        .mapNotNull(HttpEntity::getBody);
  }

  public Mono<Void> delete(Map<String, String> headers, String productCode, MenuOptionSaveRequestWrapper menuOption) {
    return webClient.delete()
        .uri(UriComponentsBuilder
            .fromUriString(restClient.getRequest().getEndpoint().concat("menu-options/{productCode}"))
            .buildAndExpand(productCode).toUriString())
        .headers(HttpHeadersFiller.fillHeaders(restClient.getRequest().getHeaders(), headers))
        .retrieve()
        .onStatus(HttpStatusCode::isError, this::handleError)
        .toEntity(Void.class)
        .mapNotNull(HttpEntity::getBody);
  }

  private Mono<? extends Throwable> handleError(ClientResponse clientResponse) {
    return errorHandler.handleError(clientResponse, ErrorDto.class, SERVICE_NAME);
  }
}