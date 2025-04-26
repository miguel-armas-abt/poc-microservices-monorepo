package com.demo.poc.entrypoint.tableorder.repository;

import com.demo.poc.commons.core.properties.restclient.RestClient;
import com.demo.poc.commons.custom.properties.ApplicationProperties;
import com.demo.poc.commons.core.restclient.WebClientFactory;
import com.demo.poc.entrypoint.tableorder.repository.wrapper.TableOrderResponseWrapper;
import com.demo.poc.entrypoint.tableorder.dto.request.MenuOrderRequestDto;
import com.demo.poc.commons.core.errors.dto.ErrorDto;
import com.demo.poc.commons.core.restclient.error.RestClientErrorHandler;
import java.util.List;
import java.util.Map;
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
public class TableOrderRepository {

  private static final String SERVICE_NAME = "table-placement-v1";

  private final RestClientErrorHandler errorHandler;
  private final WebClient webClient;
  private final RestClient restClient;

  public TableOrderRepository(ApplicationProperties properties,
                              RestClientErrorHandler errorHandler,
                              WebClientFactory webClientFactory) {
    this.errorHandler = errorHandler;

    this.restClient = properties.searchRestClient(SERVICE_NAME);
    this.webClient = webClientFactory.createWebClient(restClient.getPerformance(), SERVICE_NAME);
  }

  public Mono<Void> generateTableOrder(Map<String, String> headers,
                                       List<MenuOrderRequestDto> requestedMenuOrderList,
                                       Integer tableNumber) {
    return webClient.patch()
        .uri(UriComponentsBuilder
            .fromUriString(restClient.getRequest().getEndpoint().concat("table-orders"))
            .queryParam("tableNumber", tableNumber).toUriString())
        .headers(fillHeaders(restClient.getRequest().getHeaders(), headers))
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
            .fromUriString(restClient.getRequest().getEndpoint().concat("table-orders"))
            .queryParam("tableNumber", tableNumber).toUriString())
        .headers(fillHeaders(restClient.getRequest().getHeaders(), headers))
        .retrieve()
        .onStatus(HttpStatusCode::isError, this::handleError)
        .toEntity(TableOrderResponseWrapper.class)
        .mapNotNull(HttpEntity::getBody);
  }

  public Mono<Void> cleanTable(Map<String, String> headers, Integer tableNumber) {
    return webClient.delete()
        .uri(UriComponentsBuilder
            .fromUriString(restClient.getRequest().getEndpoint().concat("table-orders"))
            .queryParam("tableNumber", tableNumber).toUriString())
        .headers(fillHeaders(restClient.getRequest().getHeaders(), headers))
        .retrieve()
        .onStatus(HttpStatusCode::isError, this::handleError)
        .toEntity(Void.class)
        .mapNotNull(HttpEntity::getBody);
  }

  private Mono<? extends Throwable> handleError(ClientResponse clientResponse) {
    return errorHandler.handleError(clientResponse, ErrorDto.class, SERVICE_NAME);
  }
}
