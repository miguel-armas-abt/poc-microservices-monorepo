package com.demo.poc.entrypoint.tableorder.repository;

import com.demo.poc.commons.core.properties.restclient.HeaderTemplate;
import com.demo.poc.commons.custom.properties.ApplicationProperties;
import com.demo.poc.commons.core.restclient.WebClientFactory;
import com.demo.poc.entrypoint.tableorder.repository.wrapper.TableOrderResponseWrapper;
import com.demo.poc.entrypoint.tableorder.dto.request.MenuOrderRequestDto;
import com.demo.poc.commons.core.errors.dto.ErrorDto;
import com.demo.poc.commons.core.restclient.error.RestClientErrorHandler;
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

import static com.demo.poc.commons.core.restclient.utils.HttpHeadersFiller.fillHeaders;

@Repository
@RequiredArgsConstructor
public class TableOrderRepository {

  private static final String SERVICE_NAME_TABLE_PLACEMENT = "table-placement-v1";

  private final ApplicationProperties properties;
  private final RestClientErrorHandler restClientErrorHandler;
  private final WebClientFactory webClientFactory;

  private WebClient webClient;

  @PostConstruct
  public void init() {
    this.webClient = webClientFactory.createWebClient(properties.searchPerformance(SERVICE_NAME_TABLE_PLACEMENT), SERVICE_NAME_TABLE_PLACEMENT);
  }

  public Mono<Void> generateTableOrder(Map<String, String> headers,
                                       List<MenuOrderRequestDto> requestedMenuOrderList,
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
    return restClientErrorHandler.handleError(clientResponse, ErrorDto.class, SERVICE_NAME_TABLE_PLACEMENT);
  }
}
