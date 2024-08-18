package com.demo.bbq.entrypoint.sender.service;

import com.demo.bbq.entrypoint.sender.dto.PaymentSendRequestDTO;
import java.util.Map;
import reactor.core.publisher.Mono;

public interface PaymentSenderService {

  Mono<Void> sendToPay(Map<String, String> headers, PaymentSendRequestDTO paymentRequest);
}
