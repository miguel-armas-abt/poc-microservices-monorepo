package com.demo.bbq.application.service;

import com.demo.bbq.application.dto.sender.request.PaymentSendRequestDTO;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

public interface PaymentSenderService {

  Mono<Void> sendToPay(ServerRequest serverRequest, PaymentSendRequestDTO paymentRequest);
}
