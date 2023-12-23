package com.demo.bbq.business.payment.application.service;

import com.demo.bbq.business.payment.domain.model.response.Payment;
import io.reactivex.Observable;

public interface PaymentService {

  Observable<Payment> findAll();


}
