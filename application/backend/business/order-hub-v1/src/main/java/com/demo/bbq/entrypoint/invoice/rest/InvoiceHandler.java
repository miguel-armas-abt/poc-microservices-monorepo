package com.demo.bbq.entrypoint.invoice.rest;

import com.demo.bbq.commons.toolkit.validator.body.BodyValidator;
import com.demo.bbq.entrypoint.invoice.dto.PaymentSendRequestDTO;
import com.demo.bbq.entrypoint.invoice.service.InvoiceService;
import com.demo.bbq.commons.toolkit.router.ServerResponseBuilderUtil;
import com.demo.bbq.entrypoint.invoice.repository.wrapper.request.ProductRequestWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class InvoiceHandler {

  private final InvoiceService invoiceService;
  private final BodyValidator bodyValidator;

  public Mono<ServerResponse> calculateInvoice(ServerRequest serverRequest) {
    return serverRequest.bodyToFlux(ProductRequestWrapper.class)
        .doOnNext(bodyValidator::validate)
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
                .doOnNext(bodyValidator::validate)
                .flatMap(request -> invoiceService.sendToPay(serverRequest, request))
        );
  }
}