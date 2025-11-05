package com.demo.service.entrypoint.payment.service;

import com.demo.service.entrypoint.payment.dto.PaymentSendRequestDto;
import java.util.Map;
import reactor.core.publisher.Mono;

public interface PaymentSenderService {

  Mono<Void> sendToPay(Map<String, String> headers, PaymentSendRequestDto paymentRequest);
}
