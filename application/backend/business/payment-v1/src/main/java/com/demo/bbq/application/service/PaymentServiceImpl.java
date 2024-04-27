package com.demo.bbq.application.service;

import com.demo.bbq.application.events.consumer.message.PaymentMessage;
import io.reactivex.rxjava3.core.Observable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class PaymentServiceImpl implements PaymentService {
  @Override
  public Observable<PaymentMessage> findAll() {
    return null;
  }
}
