package com.demo.bbq.business.payment.application.service.impl;

import com.demo.bbq.business.payment.application.service.PaymentService;
import com.demo.bbq.business.payment.domain.model.response.Payment;
import io.reactivex.Observable;
import io.reactivex.Single;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class PaymentServiceImpl implements PaymentService {

  @Override
  public Single<Long> save() {
    return null;
  }

  @Override
  public Observable<Payment> findAll() {
    return null;
  }
}
