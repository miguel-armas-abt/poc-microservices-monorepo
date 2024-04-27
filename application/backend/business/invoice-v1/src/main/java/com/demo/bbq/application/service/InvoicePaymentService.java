package com.demo.bbq.application.service;

import com.demo.bbq.application.dto.invoicepayment.request.PaymentRequestDTO;
import io.reactivex.rxjava3.core.Completable;

public interface InvoicePaymentService {

  Completable sendToPay(PaymentRequestDTO paymentRequest);
}
