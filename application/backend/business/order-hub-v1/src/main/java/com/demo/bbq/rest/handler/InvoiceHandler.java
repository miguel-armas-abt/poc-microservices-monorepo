package com.demo.bbq.rest.handler;

import com.demo.bbq.application.dto.invoices.PaymentSendRequestDTO;
import com.demo.bbq.application.service.invoices.InvoiceService;
import com.demo.bbq.repository.invoice.wrapper.request.ProductRequestWrapper;
import com.demo.bbq.commons.toolkit.ServerResponseBuilderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class InvoiceHandler {

  private final InvoiceService invoiceService;

  public Mono<ServerResponse> calculateInvoice(ServerRequest serverRequest) {
    return serverRequest.bodyToFlux(ProductRequestWrapper.class)
        .collectList()
        .flatMap(productList -> invoiceService.calculateInvoice(serverRequest, productList))
        .flatMap(response -> ServerResponseBuilderUtil
            .buildMono(ServerResponse.ok(), serverRequest.headers(), response));
  }

  public Mono<ServerResponse> sendToPay(ServerRequest serverRequest) {
    return ServerResponseBuilderUtil
        .buildEmpty(
            ServerResponse.ok(),
            serverRequest.headers(),
            serverRequest
                .bodyToMono(PaymentSendRequestDTO.class)
                .flatMap(request -> invoiceService.sendToPay(serverRequest, request))
        );
  }
}