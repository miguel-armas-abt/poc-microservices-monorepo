package com.demo.bbq.entrypoint.sender.rest;

import com.demo.bbq.entrypoint.sender.dto.PaymentSendRequestDTO;
import com.demo.bbq.entrypoint.sender.service.PaymentSenderService;
import com.demo.bbq.commons.toolkit.router.ServerResponseBuilderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class PaymentSenderHandler {

  private final PaymentSenderService paymentSenderService;

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
