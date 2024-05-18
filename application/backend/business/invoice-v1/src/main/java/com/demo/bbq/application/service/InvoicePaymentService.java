package com.demo.bbq.application.service;

import com.demo.bbq.application.dto.invoicepayment.request.PaymentRequestDTO;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

public interface InvoicePaymentService {

  Mono<Void> sendToPay(ServerRequest serverRequest, PaymentRequestDTO paymentRequest);
}
