package com.demo.poc.entrypoint.processor.service;

import com.demo.poc.entrypoint.processor.event.order.message.PaymentOrderMessage;
import io.reactivex.rxjava3.core.Observable;

public interface PaymentService {

  Observable<PaymentOrderMessage> findAll();


}
