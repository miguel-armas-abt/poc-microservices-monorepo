package com.demo.poc.entrypoint.menu.repository;

import com.demo.poc.commons.core.properties.restclient.HeaderTemplate;
import com.demo.poc.commons.custom.properties.ApplicationProperties;
import com.demo.poc.commons.core.restclient.StreamingTransformer;
import com.demo.poc.commons.core.restclient.WebClientFactory;
import com.demo.poc.entrypoint.menu.repository.wrapper.response.MenuOptionResponseWrapper;
import com.demo.poc.entrypoint.menu.repository.wrapper.request.MenuOptionSaveRequestWrapper;
import com.demo.poc.commons.core.errors.dto.ErrorDto;
import com.demo.poc.commons.core.restclient.error.RestClientErrorHandler;
import jakarta.annotation.PostConstruct;
import java.util.Map;
import lombok.RequiredArgsConstructor;
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

import static com.demo.poc.commons.core.restclient.utils.HttpHeadersFiller.fillHeaders;

@Repository
@RequiredArgsConstructor
public class MenuV2Repository implements MenuRepository {

  private static final String SERVICE_NAME_MENU_V2 = "menu-v2";

  private final ApplicationProperties properties;
  private final RestClientErrorHandler restClientErrorHandler;
  private final WebClientFactory webClientFactory;

  private WebClient webClient;

  @PostConstruct
  public void init() {
    this.webClient = webClientFactory.createWebClient(properties.searchPerformance(SERVICE_NAME_MENU_V2), SERVICE_NAME_MENU_V2);
  }

  @Override
  public Mono<MenuOptionResponseWrapper> findByProductCode(Map<String, String> headers, String productCode) {
    return webClient.get()
        .uri(UriComponentsBuilder
            .fromUriString(getBaseURL().concat("menu-options/{productCode}"))
            .buildAndExpand(productCode).toUriString())
        .headers(fillHeaders(getHeaderTemplate(), headers))
        .retrieve()
        .onStatus(HttpStatusCode::isError, this::handleError)
        .toEntity(MenuOptionResponseWrapper.class)
        .mapNotNull(HttpEntity::getBody);
  }

  @Override
  public Flux<MenuOptionResponseWrapper> findByCategory(Map<String, String> headers, String category) {
    return webClient.get()
        .uri(UriComponentsBuilder
            .fromUriString(getBaseURL().concat("menu-options"))
            .queryParam("category", category).toUriString())
        .accept(MediaType.APPLICATION_NDJSON)
        .headers(fillHeaders(getHeaderTemplate(), headers))
        .retrieve()
        .onStatus(HttpStatusCode::isError, this::handleError)
        .bodyToFlux(String.class)
        .flatMap(StreamingTransformer.of(MenuOptionResponseWrapper.class));
  }

  @Override
  public Mono<Void> save(Map<String, String> headers, MenuOptionSaveRequestWrapper menuOption) {
    return webClient.post()
        .uri(UriComponentsBuilder
            .fromUriString(getBaseURL().concat("menu-options")).toUriString())
        .contentType(MediaType.APPLICATION_JSON)
        .headers(fillHeaders(getHeaderTemplate(), headers))
        .body(BodyInserters.fromValue(menuOption))
        .retrieve()
        .onStatus(HttpStatusCode::isError, this::handleError)
        .toEntity(Void.class)
        .mapNotNull(HttpEntity::getBody);
  }

  @Override
  public Mono<Void> update(Map<String, String> headers, String productCode, MenuOptionSaveRequestWrapper menuOption) {
    return webClient.post()
        .uri(UriComponentsBuilder
            .fromUriString(getBaseURL().concat("menu-options/{productCode}"))
            .buildAndExpand(productCode).toUriString())
        .contentType(MediaType.APPLICATION_JSON)
        .headers(fillHeaders(getHeaderTemplate(), headers))
        .body(BodyInserters.fromValue(menuOption))
        .retrieve()
        .onStatus(HttpStatusCode::isError, this::handleError)
        .toEntity(Void.class)
        .mapNotNull(HttpEntity::getBody);
  }

  @Override
  public Mono<Void> delete(Map<String, String> headers, String productCode, MenuOptionSaveRequestWrapper menuOption) {
    return webClient.delete()
        .uri(UriComponentsBuilder
            .fromUriString(getBaseURL().concat("menu-options/{productCode}"))
            .buildAndExpand(productCode).toUriString())
        .headers(fillHeaders(getHeaderTemplate(), headers))
        .retrieve()
        .onStatus(HttpStatusCode::isError, this::handleError)
        .toEntity(Void.class)
        .mapNotNull(HttpEntity::getBody);
  }

  private String getBaseURL() {
    return properties.searchEndpoint(SERVICE_NAME_MENU_V2);
  }

  private HeaderTemplate getHeaderTemplate() {
    return properties.searchHeaderTemplate(SERVICE_NAME_MENU_V2);
  }

  private Mono<? extends Throwable> handleError(ClientResponse clientResponse) {
    return restClientErrorHandler.handleError(clientResponse, ErrorDto.class, SERVICE_NAME_MENU_V2);
  }

  @Override
  public boolean supports(Class<?> selectedClass) {
    return this.getClass().isAssignableFrom(selectedClass);
  }
}