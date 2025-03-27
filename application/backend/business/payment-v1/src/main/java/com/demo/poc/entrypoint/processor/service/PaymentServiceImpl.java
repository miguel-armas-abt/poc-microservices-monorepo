package com.demo.poc.entrypoint.processor.service;

import com.demo.poc.entrypoint.processor.event.order.message.PaymentOrderMessage;
import io.reactivex.rxjava3.core.Observable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class PaymentServiceImpl implements PaymentService {
  @Override
  public Observable<PaymentOrderMessage> findAll() {
    return null;
  }
}
