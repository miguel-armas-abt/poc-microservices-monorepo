package com.demo.bbq.business.payment.application.service;

import com.demo.bbq.business.payment.domain.model.response.Payment;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * <br/>Interface Service que define los métodos necesarios para tramitar la lógica de negocio del contexto Payment.<br/>
 *
 * @author Miguel Armas Abt <br/>
 */
public interface PaymentService {

  Single<Long> save();

  Observable<Payment> findAll();
}
