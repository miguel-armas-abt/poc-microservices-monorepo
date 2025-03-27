package com.demo.poc.entrypoint.sender.service;

import com.demo.poc.entrypoint.sender.dto.PaymentSendRequestDTO;
import java.util.Map;
import reactor.core.publisher.Mono;

public interface PaymentSenderService {

  Mono<Void> sendToPay(Map<String, String> headers, PaymentSendRequestDTO paymentRequest);
}
