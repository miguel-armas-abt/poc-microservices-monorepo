package com.demo.bbq.repository.menu;

import static com.demo.bbq.utils.restclient.headers.HeadersBuilderUtil.buildHeaders;

import com.demo.bbq.config.errors.handler.external.ExternalErrorHandler;
import com.demo.bbq.config.properties.ServiceConfigurationProperties;
import com.demo.bbq.repository.menu.wrapper.response.MenuOptionResponseWrapper;
import com.demo.bbq.repository.menu.wrapper.request.MenuOptionSaveRequestWrapper;
import com.demo.bbq.utils.errors.dto.ErrorDTO;
import com.demo.bbq.utils.properties.dto.HeaderTemplate;
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
public class MenuV1Repository implements MenuRepository {

  private static final String SERVICE_NAME_MENU_V1 = "menu-v1";

  private final WebClient webClient;
  private final ServiceConfigurationProperties properties;
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