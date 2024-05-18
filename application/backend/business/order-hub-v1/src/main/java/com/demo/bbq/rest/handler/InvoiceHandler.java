package com.demo.bbq.rest.handler;

import com.demo.bbq.application.dto.invoices.InvoicePaymentRequestDTO;
import com.demo.bbq.application.service.invoices.InvoiceService;
import com.demo.bbq.repository.invoice.wrapper.request.ProductRequestWrapper;
import com.demo.bbq.repository.invoice.wrapper.response.ProformaInvoiceResponseWrapper;
import com.demo.bbq.rest.common.BuilderServerResponse;
import com.newrelic.api.agent.Trace;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class InvoiceHandler {

  private final InvoiceService invoiceService;
  private final BuilderServerResponse<ProformaInvoiceResponseWrapper> builderServerResponse;

  @Trace(dispatcher = true)
  public Mono<ServerResponse> generateProforma(ServerRequest serverRequest) {
    return serverRequest.bodyToFlux(ProductRequestWrapper.class)
        .collectList()
        .flatMap(productList -> invoiceService.generateProforma(serverRequest, productList))
        .flatMap(builderServerResponse::build);
  }

  @Trace(dispatcher = true)
  public Mono<ServerResponse> sendToPay(ServerRequest serverRequest) {
    return builderServerResponse.buildVoid(serverRequest.bodyToMono(InvoicePaymentRequestDTO.class)
        .flatMap(request -> invoiceService.sendToPay(serverRequest, request)));
  }
}
