package com.demo.poc.entrypoint.invoice.repository;

import com.demo.poc.commons.core.properties.restclient.HeaderTemplate;
import com.demo.poc.commons.custom.properties.ApplicationProperties;
import com.demo.poc.commons.core.restclient.WebClientFactory;
import com.demo.poc.entrypoint.invoice.repository.wrapper.request.PaymentSendRequestWrapper;
import com.demo.poc.entrypoint.invoice.repository.wrapper.request.ProductRequestWrapper;
import com.demo.poc.entrypoint.invoice.repository.wrapper.response.InvoiceResponseWrapper;
import com.demo.poc.commons.core.errors.dto.ErrorDto;
import com.demo.poc.commons.core.errors.external.ExternalErrorHandler;
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
import reactor.core.publisher.Mono;

import static com.demo.poc.commons.core.restclient.utils.HttpHeadersFiller.fillHeaders;

@Repository
@RequiredArgsConstructor
public class InvoiceRepository {

  private static final String SERVICE_NAME_INVOICE = "invoice-v1";

  private final ApplicationProperties properties;
  private final ExternalErrorHandler externalErrorHandler;
  private final WebClientFactory webClientFactory;

  private WebClient webClient;

  @PostConstruct
  public void init() {
    this.webClient = webClientFactory.createWebClient(properties.searchPerformance(SERVICE_NAME_INVOICE), SERVICE_NAME_INVOICE);
  }

  public Mono<InvoiceResponseWrapper> generateProforma(Map<String, String> headers,
                                                       List<ProductRequestWrapper> productList) {
    return webClient.post()
        .uri(getBaseURL().concat("calculate"))
        .contentType(MediaType.APPLICATION_JSON)
        .headers(fillHeaders(properties.searchHeaderTemplate(SERVICE_NAME_INVOICE), headers))
        .body(BodyInserters.fromValue(productList))
        .retrieve()
        .onStatus(HttpStatusCode::isError, this::handleError)
        .toEntity(InvoiceResponseWrapper.class)
        .mapNotNull(HttpEntity::getBody);
  }

  public Mono<Void> sendToPay(Map<String, String> headers,
                              PaymentSendRequestWrapper paymentRequest) {
    return webClient.post()
        .uri(getBaseURL().concat("send-to-pay"))
        .contentType(MediaType.APPLICATION_JSON)
        .headers(fillHeaders(getHeaderTemplate(), headers))
        .body(BodyInserters.fromValue(paymentRequest))
        .retrieve()
        .onStatus(HttpStatusCode::isError, this::handleError)
        .toEntity(Void.class)
        .mapNotNull(HttpEntity::getBody);
  }

  private String getBaseURL() {
    return properties.searchEndpoint(SERVICE_NAME_INVOICE);
  }

  private HeaderTemplate getHeaderTemplate() {
    return properties.searchHeaderTemplate(SERVICE_NAME_INVOICE);
  }

  private Mono<? extends Throwable> handleError(ClientResponse clientResponse) {
    return externalErrorHandler.handleError(clientResponse, ErrorDto.class, SERVICE_NAME_INVOICE);
  }
}



















