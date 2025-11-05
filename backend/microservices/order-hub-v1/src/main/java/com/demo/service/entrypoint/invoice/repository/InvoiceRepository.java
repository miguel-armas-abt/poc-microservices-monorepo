package com.demo.service.entrypoint.invoice.repository;

import com.demo.commons.errors.dto.ErrorDto;
import com.demo.commons.properties.restclient.RestClient;
import com.demo.commons.restclient.error.RestClientErrorHandler;
import com.demo.commons.restclient.utils.HttpHeadersFiller;
import com.demo.service.commons.properties.ApplicationProperties;
import com.demo.commons.restclient.WebClientFactory;
import com.demo.service.entrypoint.invoice.repository.wrapper.request.PaymentSendRequestWrapper;
import com.demo.service.entrypoint.invoice.repository.wrapper.request.ProductRequestWrapper;
import com.demo.service.entrypoint.invoice.repository.wrapper.response.InvoiceResponseWrapper;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Repository
public class InvoiceRepository {

  private static final String SERVICE_NAME = "invoice-v1";

  private final RestClientErrorHandler errorHandler;
  private final WebClient webClient;
  private final RestClient restClient;

  public InvoiceRepository(ApplicationProperties properties,
                           RestClientErrorHandler errorHandler,
                           WebClientFactory webClientFactory) {
    this.errorHandler = errorHandler;

    this.restClient = properties.searchRestClient(SERVICE_NAME);
    this.webClient = webClientFactory.createWebClient(restClient.getPerformance(), SERVICE_NAME);
  }

  public Mono<InvoiceResponseWrapper> generateProforma(Map<String, String> headers,
                                                       List<ProductRequestWrapper> productList) {
    return webClient.post()
        .uri(restClient.getRequest().getEndpoint().concat("calculate"))
        .contentType(MediaType.APPLICATION_JSON)
        .headers(HttpHeadersFiller.fillHeaders(restClient.getRequest().getHeaders(), headers))
        .body(BodyInserters.fromValue(productList))
        .retrieve()
        .onStatus(HttpStatusCode::isError, this::handleError)
        .toEntity(InvoiceResponseWrapper.class)
        .mapNotNull(HttpEntity::getBody);
  }

  public Mono<Void> sendToPay(Map<String, String> headers,
                              PaymentSendRequestWrapper paymentRequest) {
    return webClient.post()
        .uri(restClient.getRequest().getEndpoint().concat("send-to-pay"))
        .contentType(MediaType.APPLICATION_JSON)
        .headers(HttpHeadersFiller.fillHeaders(restClient.getRequest().getHeaders(), headers))
        .body(BodyInserters.fromValue(paymentRequest))
        .retrieve()
        .onStatus(HttpStatusCode::isError, this::handleError)
        .toEntity(Void.class)
        .mapNotNull(HttpEntity::getBody);
  }

  private Mono<? extends Throwable> handleError(ClientResponse clientResponse) {
    return errorHandler.handleError(clientResponse, ErrorDto.class, SERVICE_NAME);
  }
}



















