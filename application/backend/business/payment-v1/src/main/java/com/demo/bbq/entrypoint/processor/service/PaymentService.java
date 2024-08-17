package com.demo.bbq.entrypoint.processor.service;

import com.demo.bbq.entrypoint.processor.event.order.message.PaymentOrderMessage;
import io.reactivex.rxjava3.core.Observable;

public interface PaymentService {

  Observable<PaymentOrderMessage> findAll();


}
