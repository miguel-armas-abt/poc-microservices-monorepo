package com.demo.poc.entrypoint.payment.service;

import com.demo.poc.entrypoint.payment.event.pay.message.PaymentInboundMessage;
import io.reactivex.rxjava3.core.Observable;

public interface PaymentService {

  Observable<PaymentInboundMessage> findAll();


}
