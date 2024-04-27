package com.demo.bbq.application.service;

import com.demo.bbq.application.events.consumer.message.PaymentMessage;
import io.reactivex.rxjava3.core.Observable;

public interface PaymentService {

  Observable<PaymentMessage> findAll();


}
