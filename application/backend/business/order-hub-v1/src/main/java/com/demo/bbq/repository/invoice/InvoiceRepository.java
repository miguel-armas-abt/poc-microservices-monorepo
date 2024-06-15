package com.demo.bbq.repository.invoice;

import static com.demo.bbq.commons.restclient.headers.HeadersBuilderUtil.buildHeaders;

import com.demo.bbq.config.properties.ServiceConfigurationProperties;
import com.demo.bbq.repository.invoice.wrapper.request.PaymentSendRequestWrapper;
import com.demo.bbq.repository.invoice.wrapper.request.ProductRequestWrapper;
import com.demo.bbq.repository.invoice.wrapper.response.InvoiceResponseWrapper;
import com.demo.bbq.commons.errors.dto.ErrorDTO;
import com.demo.bbq.commons.errors.handler.external.ExternalErrorHandler;
import com.demo.bbq.commons.properties.dto.HeaderTemplate;
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
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class InvoiceRepository {

  private static final String SERVICE_NAME_INVOICE = "invoice-v1";

  private final WebClient webClient;
  private final ServiceConfigurationProperties properties;
  private final ExternalErrorHandler externalErrorHandler;

  public Mono<InvoiceResponseWrapper> generateProforma(ServerRequest serverRequest,
                                                       List<ProductRequestWrapper> productList) {
    return webClient.post()
        .uri(getBaseURL().concat("calculate"))
        .contentType(MediaType.APPLICATION_JSON)
        .headers(buildHeaders(properties.searchHeaderTemplate(SERVICE_NAME_INVOICE), serverRequest))
        .body(BodyInserters.fromValue(productList))
        .retrieve()
        .onStatus(HttpStatusCode::isError, this::handleError)
        .toEntity(InvoiceResponseWrapper.class)
        .mapNotNull(HttpEntity::getBody);
  }

  public Mono<Void> sendToPay(ServerRequest serverRequest,
                                     PaymentSendRequestWrapper paymentRequest) {
    return webClient.post()
        .uri(getBaseURL().concat("send-to-pay"))
        .contentType(MediaType.APPLICATION_JSON)
        .headers(buildHeaders(getHeaderTemplate(), serverRequest))
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
    return externalErrorHandler.handleError(clientResponse, ErrorDTO.class, SERVICE_NAME_INVOICE);
  }
}



















