package com.demo.bbq.entrypoint.menu.repository;

import static com.demo.bbq.commons.restclient.headers.HeadersBuilderUtil.buildHeaders;

import com.demo.bbq.commons.properties.dto.restclient.HeaderTemplate;
import com.demo.bbq.commons.properties.ApplicationProperties;
import com.demo.bbq.entrypoint.menu.repository.wrapper.response.MenuOptionResponseWrapper;
import com.demo.bbq.entrypoint.menu.repository.wrapper.request.MenuOptionSaveRequestWrapper;
import com.demo.bbq.commons.errors.dto.ErrorDTO;
import com.demo.bbq.commons.errors.handler.external.ExternalErrorHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class MenuV2Repository implements MenuRepository {

  private static final String SERVICE_NAME_MENU_V2 = "menu-v2";

  private final WebClient webClient;
  private final ApplicationProperties properties;
  private final ExternalErrorHandler externalErrorHandler;

  @Override
  public Mono<MenuOptionResponseWrapper> findByProductCode(ServerRequest serverRequest, String productCode) {
    return webClient.get()
        .uri(UriComponentsBuilder
            .fromUriString(getBaseURL().concat("menu-options/{productCode}"))
            .buildAndExpand(productCode).toUriString())
        .headers(buildHeaders(getHeaderTemplate(), serverRequest))
        .retrieve()
        .onStatus(HttpStatusCode::isError, this::handleError)
        .toEntity(MenuOptionResponseWrapper.class)
        .mapNotNull(HttpEntity::getBody);
  }

  @Override
  public Flux<MenuOptionResponseWrapper> findByCategory(ServerRequest serverRequest, String category) {
    return webClient.get()
        .uri(UriComponentsBuilder
            .fromUriString(getBaseURL().concat("menu-options"))
            .queryParam("category", category).toUriString())
        .headers(buildHeaders(getHeaderTemplate(), serverRequest))
        .retrieve()
        .onStatus(HttpStatusCode::isError, this::handleError)
        .onStatus(HttpStatusCode::isError, clientResponse -> externalErrorHandler.handleError(clientResponse, ErrorDTO.class, SERVICE_NAME_MENU_V2))
        .bodyToFlux(MenuOptionResponseWrapper.class);
  }

  @Override
  public Mono<Void> save(ServerRequest serverRequest, MenuOptionSaveRequestWrapper menuOption) {
    return webClient.post()
        .uri(UriComponentsBuilder
            .fromUriString(getBaseURL().concat("menu-options")).toUriString())
        .contentType(MediaType.APPLICATION_JSON)
        .headers(buildHeaders(getHeaderTemplate(), serverRequest))
        .body(BodyInserters.fromValue(menuOption))
        .retrieve()
        .onStatus(HttpStatusCode::isError, this::handleError)
        .toEntity(Void.class)
        .mapNotNull(HttpEntity::getBody);
  }

  @Override
  public Mono<Void> update(ServerRequest serverRequest, String productCode, MenuOptionSaveRequestWrapper menuOption) {
    return webClient.post()
        .uri(UriComponentsBuilder
            .fromUriString(getBaseURL().concat("menu-options/{productCode}"))
            .buildAndExpand(productCode).toUriString())
        .contentType(MediaType.APPLICATION_JSON)
        .headers(buildHeaders(getHeaderTemplate(), serverRequest))
        .body(BodyInserters.fromValue(menuOption))
        .retrieve()
        .onStatus(HttpStatusCode::isError, this::handleError)
        .toEntity(Void.class)
        .mapNotNull(HttpEntity::getBody);
  }

  @Override
  public Mono<Void> delete(ServerRequest serverRequest, String productCode, MenuOptionSaveRequestWrapper menuOption) {
    return webClient.delete()
        .uri(UriComponentsBuilder
            .fromUriString(getBaseURL().concat("menu-options/{productCode}"))
            .buildAndExpand(productCode).toUriString())
        .headers(buildHeaders(getHeaderTemplate(), serverRequest))
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
    return externalErrorHandler.handleError(clientResponse, ErrorDTO.class, SERVICE_NAME_MENU_V2);
  }

  @Override
  public boolean supports(Class<?> selectedClass) {
    return this.getClass().isAssignableFrom(selectedClass);
  }
}