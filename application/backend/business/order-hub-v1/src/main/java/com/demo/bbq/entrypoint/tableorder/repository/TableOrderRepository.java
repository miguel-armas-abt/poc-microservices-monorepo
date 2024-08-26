package com.demo.bbq.entrypoint.tableorder.repository;

import static com.demo.bbq.commons.toolkit.params.filler.HttpHeadersFiller.fillHeaders;

import com.demo.bbq.commons.properties.dto.restclient.HeaderTemplate;
import com.demo.bbq.commons.properties.ApplicationProperties;
import com.demo.bbq.commons.restclient.webclient.WebClientFactory;
import com.demo.bbq.entrypoint.tableorder.repository.wrapper.TableOrderResponseWrapper;
import com.demo.bbq.entrypoint.tableorder.dto.request.MenuOrderRequestDTO;
import com.demo.bbq.commons.errors.dto.ErrorDTO;
import com.demo.bbq.commons.errors.handler.external.ExternalErrorHandler;
import jakarta.annotation.PostConstruct;
import java.util.List;
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
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class TableOrderRepository {

  private static final String SERVICE_NAME_TABLE_PLACEMENT = "table-placement-v1";

  private final ApplicationProperties properties;
  private final ExternalErrorHandler externalErrorHandler;
  private final WebClientFactory webClientFactory;

  private WebClient webClient;

  @PostConstruct
  public void init() {
    this.webClient = webClientFactory.createWebClient(properties.searchPerformance(SERVICE_NAME_TABLE_PLACEMENT), SERVICE_NAME_TABLE_PLACEMENT);
  }

  public Mono<Void> generateTableOrder(Map<String, String> headers,
                                       List<MenuOrderRequestDTO> requestedMenuOrderList,
                                       Integer tableNumber) {
    return webClient.patch()
        .uri(UriComponentsBuilder
            .fromUriString(getBaseURL().concat("table-orders"))
            .queryParam("tableNumber", tableNumber).toUriString())
        .headers(fillHeaders(getHeaderTemplate(), headers))
        .contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(requestedMenuOrderList))
        .retrieve()
        .onStatus(HttpStatusCode::isError, this::handleError)
        .toEntity(Void.class)
        .mapNotNull(HttpEntity::getBody);
  }

  public Mono<TableOrderResponseWrapper> findByTableNumber(Map<String, String> headers,
                                                           Integer tableNumber) {
    return webClient.get()
        .uri(UriComponentsBuilder
            .fromUriString(getBaseURL().concat("table-orders"))
            .queryParam("tableNumber", tableNumber).toUriString())
        .headers(fillHeaders(getHeaderTemplate(), headers))
        .retrieve()
        .onStatus(HttpStatusCode::isError, this::handleError)
        .toEntity(TableOrderResponseWrapper.class)
        .mapNotNull(HttpEntity::getBody);
  }

  public Mono<Void> cleanTable(Map<String, String> headers, Integer tableNumber) {
    return webClient.delete()
        .uri(UriComponentsBuilder
            .fromUriString(getBaseURL().concat("table-orders"))
            .queryParam("tableNumber", tableNumber).toUriString())
        .headers(fillHeaders(getHeaderTemplate(), headers))
        .retrieve()
        .onStatus(HttpStatusCode::isError, this::handleError)
        .toEntity(Void.class)
        .mapNotNull(HttpEntity::getBody);
  }

  private String getBaseURL() {
    return properties.searchEndpoint(SERVICE_NAME_TABLE_PLACEMENT);
  }

  private HeaderTemplate getHeaderTemplate() {
    return properties.searchHeaderTemplate(SERVICE_NAME_TABLE_PLACEMENT);
  }

  private Mono<? extends Throwable> handleError(ClientResponse clientResponse) {
    return externalErrorHandler.handleError(clientResponse, ErrorDTO.class, SERVICE_NAME_TABLE_PLACEMENT);
  }
}
