package com.demo.bbq.repository.tableorder;

import static com.demo.bbq.commons.restclient.headers.HeadersBuilderUtil.buildHeaders;

import com.demo.bbq.commons.properties.dto.restclient.HeaderTemplate;
import com.demo.bbq.config.properties.ApplicationProperties;
import com.demo.bbq.repository.tableorder.wrapper.TableOrderResponseWrapper;
import com.demo.bbq.application.dto.tableorder.request.MenuOrderRequestDTO;
import com.demo.bbq.commons.errors.dto.ErrorDTO;
import com.demo.bbq.commons.errors.handler.external.ExternalErrorHandler;
import java.util.List;
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
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class TableOrderRepository {

  private static final String SERVICE_NAME_TABLE_PLACEMENT = "table-placement-v1";

  private final WebClient webClient;
  private final ApplicationProperties properties;
  private final ExternalErrorHandler externalErrorHandler;

  public Mono<Void> generateTableOrder(ServerRequest serverRequest,
                                       List<MenuOrderRequestDTO> requestedMenuOrderList,
                                       Integer tableNumber) {
    return webClient.patch()
        .uri(UriComponentsBuilder
            .fromUriString(getBaseURL().concat("table-orders"))
            .queryParam("tableNumber", tableNumber).toUriString())
        .headers(buildHeaders(getHeaderTemplate(), serverRequest))
        .contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(requestedMenuOrderList))
        .retrieve()
        .onStatus(HttpStatusCode::isError, this::handleError)
        .toEntity(Void.class)
        .mapNotNull(HttpEntity::getBody);
  }

  public Mono<TableOrderResponseWrapper> findByTableNumber(ServerRequest serverRequest,
                                                           Integer tableNumber) {
    return webClient.get()
        .uri(UriComponentsBuilder
            .fromUriString(getBaseURL().concat("table-orders"))
            .queryParam("tableNumber", tableNumber).toUriString())
        .headers(buildHeaders(getHeaderTemplate(), serverRequest))
        .retrieve()
        .onStatus(HttpStatusCode::isError, this::handleError)
        .toEntity(TableOrderResponseWrapper.class)
        .mapNotNull(HttpEntity::getBody);
  }

  public Mono<Void> cleanTable(ServerRequest serverRequest, Integer tableNumber) {
    return webClient.delete()
        .uri(UriComponentsBuilder
            .fromUriString(getBaseURL().concat("table-orders"))
            .queryParam("tableNumber", tableNumber).toUriString())
        .headers(buildHeaders(getHeaderTemplate(), serverRequest))
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
