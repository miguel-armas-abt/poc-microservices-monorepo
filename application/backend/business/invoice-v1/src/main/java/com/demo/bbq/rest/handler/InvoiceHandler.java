package com.demo.bbq.rest.handler;

import com.demo.bbq.application.dto.invoicepayment.request.PaymentRequestDTO;
import com.demo.bbq.application.dto.proformainvoice.request.ProductRequestDTO;
import com.demo.bbq.application.dto.proformainvoice.response.ProformaInvoiceResponseDTO;
import com.demo.bbq.application.service.InvoicePaymentService;
import com.demo.bbq.application.service.ProformaInvoiceService;
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

  private final ProformaInvoiceService proformaInvoiceService;
  private final InvoicePaymentService invoicePaymentService;
  private final BuilderServerResponse<ProformaInvoiceResponseDTO> builderServerResponse;

  @Trace(dispatcher = true)
  public Mono<ServerResponse> generateProforma(ServerRequest serverRequest) {
    return proformaInvoiceService.generateProformaInvoice(serverRequest, serverRequest.bodyToFlux(ProductRequestDTO.class))
        .flatMap(builderServerResponse::build);
  }

  @Trace(dispatcher = true)
  public Mono<ServerResponse> sendToPay(ServerRequest serverRequest) {
    return builderServerResponse.buildVoid(serverRequest.bodyToMono(PaymentRequestDTO.class)
        .flatMap(request -> invoicePaymentService.sendToPay(serverRequest, request)));
  }
}
