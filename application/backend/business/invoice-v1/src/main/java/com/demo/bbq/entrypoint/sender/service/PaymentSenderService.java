package com.demo.bbq.entrypoint.sender.service;

import com.demo.bbq.entrypoint.sender.dto.PaymentSendRequestDTO;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

public interface PaymentSenderService {

  Mono<Void> sendToPay(ServerRequest serverRequest, PaymentSendRequestDTO paymentRequest);
}
