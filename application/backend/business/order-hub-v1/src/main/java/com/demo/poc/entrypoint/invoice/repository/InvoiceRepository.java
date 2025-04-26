package com.demo.poc.entrypoint.invoice.repository;

import com.demo.poc.commons.core.properties.restclient.RestClient;
import com.demo.poc.commons.custom.properties.ApplicationProperties;
import com.demo.poc.commons.core.restclient.WebClientFactory;
import com.demo.poc.entrypoint.invoice.repository.wrapper.request.PaymentSendRequestWrapper;
import com.demo.poc.entrypoint.invoice.repository.wrapper.request.ProductRequestWrapper;
import com.demo.poc.entrypoint.invoice.repository.wrapper.response.InvoiceResponseWrapper;
import com.demo.poc.commons.core.errors.dto.ErrorDto;
import com.demo.poc.commons.core.restclient.error.RestClientErrorHandler;
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
import reactor.core.publisher.Mono;

import static com.demo.poc.commons.core.restclient.utils.HttpHeadersFiller.fillHeaders;

@Repository
@RequiredArgsConstructor
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
        .headers(fillHeaders(restClient.getRequest().getHeaders(), headers))
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
        .headers(fillHeaders(restClient.getRequest().getHeaders(), headers))
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



















