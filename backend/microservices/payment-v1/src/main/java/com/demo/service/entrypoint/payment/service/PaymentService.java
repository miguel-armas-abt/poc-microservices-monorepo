package com.demo.service.entrypoint.payment.service;

import com.demo.service.entrypoint.payment.event.pay.message.PaymentInboundMessage;
import io.reactivex.rxjava3.core.Observable;

public interface PaymentService {

  Observable<PaymentInboundMessage> findAll();


}
