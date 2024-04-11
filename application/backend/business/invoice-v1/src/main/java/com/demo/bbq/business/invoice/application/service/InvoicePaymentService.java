package com.demo.bbq.business.invoice.application.service;

import com.demo.bbq.business.invoice.application.dto.invoicepayment.request.PaymentRequestDTO;
import io.reactivex.rxjava3.core.Completable;

public interface InvoicePaymentService {

  Completable sendToPay(PaymentRequestDTO paymentRequest);
}
