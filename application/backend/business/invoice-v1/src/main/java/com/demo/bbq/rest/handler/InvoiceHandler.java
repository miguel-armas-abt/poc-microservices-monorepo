package com.demo.bbq.rest.handler;

import com.demo.bbq.application.dto.sender.request.PaymentSendRequestDTO;
import com.demo.bbq.application.dto.calculator.request.ProductRequestDTO;
import com.demo.bbq.application.service.PaymentSenderService;
import com.demo.bbq.application.service.CalculatorService;
import com.demo.bbq.commons.toolkit.router.ServerResponseBuilderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class InvoiceHandler {

  private final CalculatorService calculatorService;
  private final PaymentSenderService paymentSenderService;

  public Mono<ServerResponse> calculateInvoice(ServerRequest serverRequest) {
    return calculatorService.calculateInvoice(serverRequest, serverRequest.bodyToFlux(ProductRequestDTO.class))
        .flatMap(response -> ServerResponseBuilderUtil
            .buildMono(ServerResponse.ok(), serverRequest.headers(), response));
  }

  public Mono<ServerResponse> sendToPay(ServerRequest serverRequest) {
    return ServerResponseBuilderUtil
        .buildEmpty(
            ServerResponse.ok(),
            serverRequest.headers(),
            serverRequest.bodyToMono(PaymentSendRequestDTO.class)
                .flatMap(request -> paymentSenderService.sendToPay(serverRequest, request))
        );
  }
}
