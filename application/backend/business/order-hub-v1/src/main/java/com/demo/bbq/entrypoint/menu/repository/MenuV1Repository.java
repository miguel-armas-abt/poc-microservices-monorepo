package com.demo.bbq.entrypoint.menu.repository;

import static com.demo.bbq.commons.toolkit.params.filler.HeadersFiller.buildHeaders;

import com.demo.bbq.commons.properties.dto.restclient.HeaderTemplate;
import com.demo.bbq.commons.properties.ApplicationProperties;
import com.demo.bbq.commons.restclient.webclient.WebClientFactory;
import com.demo.bbq.entrypoint.menu.repository.wrapper.response.MenuOptionResponseWrapper;
import com.demo.bbq.entrypoint.menu.repository.wrapper.request.MenuOptionSaveRequestWrapper;
import com.demo.bbq.commons.errors.dto.ErrorDTO;
import com.demo.bbq.commons.errors.handler.external.ExternalErrorHandler;
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

@Repository
@RequiredArgsConstructor
public class MenuV1Repository implements MenuRepository {

  private static final String SERVICE_NAME_MENU_V1 = "menu-v1";

  private final ApplicationProperties properties;
  private final ExternalErrorHandler externalErrorHandler;
  private final WebClientFactory webClientFactory;

  private WebClient webClient;

  @PostConstruct
  public void init() {
    this.webClient = webClientFactory.createWebClient(properties.searchPerformance(SERVICE_NAME_MENU_V1), SERVICE_NAME_MENU_V1);
  }

  @Override
  public Mono<MenuOptionResponseWrapper> findByProductCode(Map<String, String> headers, String productCode) {
    return webClient.get()
        .uri(UriComponentsBuilder
            .fromUriString(getBaseURL().concat("menu-options/{productCode}"))
            .buildAndExpand(productCode).toUriString())
        .headers(buildHeaders(getHeaderTemplate(), headers))
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
        .headers(buildHeaders(getHeaderTemplate(), headers))
        .retrieve()
        .onStatus(HttpStatusCode::isError, this::handleError)
        .bodyToFlux(MenuOptionResponseWrapper.class);
  }

  @Override
  public Mono<Void> save(Map<String, String> headers, MenuOptionSaveRequestWrapper menuOption) {
    return webClient.post()
        .uri(UriComponentsBuilder
            .fromUriString(getBaseURL().concat("menu-options")).toUriString())
        .contentType(MediaType.APPLICATION_JSON)
        .headers(buildHeaders(getHeaderTemplate(), headers))
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
        .headers(buildHeaders(getHeaderTemplate(), headers))
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
        .headers(buildHeaders(getHeaderTemplate(), headers))
        .retrieve()
        .onStatus(HttpStatusCode::isError, this::handleError)
        .toEntity(Void.class)
        .mapNotNull(HttpEntity::getBody);
  }

  private String getBaseURL() {
    return properties.searchEndpoint(SERVICE_NAME_MENU_V1);
  }

  private HeaderTemplate getHeaderTemplate() {
    return properties.searchHeaderTemplate(SERVICE_NAME_MENU_V1);
  }

  private Mono<? extends Throwable> handleError(ClientResponse clientResponse) {
    return externalErrorHandler.handleError(clientResponse, ErrorDTO.class, SERVICE_NAME_MENU_V1);
  }

  @Override
  public boolean supports(Class<?> selectedClass) {
    return this.getClass().isAssignableFrom(selectedClass);
  }
}