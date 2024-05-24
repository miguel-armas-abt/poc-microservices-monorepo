package com.demo.bbq.rest.handler;

import com.demo.bbq.application.dto.invoicepayment.request.PaymentRequestDTO;
import com.demo.bbq.application.dto.proformainvoice.request.ProductRequestDTO;
import com.demo.bbq.application.service.InvoicePaymentService;
import com.demo.bbq.application.service.ProformaInvoiceService;
import com.demo.bbq.utils.toolkit.ServerResponseBuilderUtil;
import com.newrelic.api.agent.Trace;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class InvoiceHandler {

  private final ProformaInvoiceService proformaInvoiceService;
  private final InvoicePaymentService invoicePaymentService;

  @Trace(dispatcher = true)
  public Mono<ServerResponse> generateProforma(ServerRequest serverRequest) {
    return proformaInvoiceService.generateProforma(serverRequest, serverRequest.bodyToFlux(ProductRequestDTO.class))
        .flatMap(response -> ServerResponseBuilderUtil
            .buildMono(ServerResponse.ok(), serverRequest.headers(), response));
  }

  @Trace(dispatcher = true)
  public Mono<ServerResponse> sendToPay(ServerRequest serverRequest) {
    return ServerResponseBuilderUtil
        .buildEmpty(
            ServerResponse.ok(),
            serverRequest.headers(),
            serverRequest.bodyToMono(PaymentRequestDTO.class)
                .flatMap(request -> invoicePaymentService.sendToPay(serverRequest, request))
        );
  }
}
