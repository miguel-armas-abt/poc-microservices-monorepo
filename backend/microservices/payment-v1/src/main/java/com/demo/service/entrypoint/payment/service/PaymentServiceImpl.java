package com.demo.service.entrypoint.payment.service;

import com.demo.service.entrypoint.payment.event.pay.message.PaymentInboundMessage;
import io.reactivex.rxjava3.core.Observable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class PaymentServiceImpl implements PaymentService {
  @Override
  public Observable<PaymentInboundMessage> findAll() {
    return null;
  }
}
