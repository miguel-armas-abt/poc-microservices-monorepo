package com.demo.bbq.business.payment.application.service;

import com.demo.bbq.business.payment.application.events.consumer.message.PaymentMessage;
import io.reactivex.Observable;

public interface PaymentService {

  Observable<PaymentMessage> findAll();


}
