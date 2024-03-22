package com.demo.bbq.business.invoice.application.service;

import com.demo.bbq.business.invoice.application.dto.invoicepayment.request.PaymentRequest;
import io.reactivex.Completable;

public interface InvoicePaymentService {

  Completable sendToPay(PaymentRequest paymentRequest);
}
