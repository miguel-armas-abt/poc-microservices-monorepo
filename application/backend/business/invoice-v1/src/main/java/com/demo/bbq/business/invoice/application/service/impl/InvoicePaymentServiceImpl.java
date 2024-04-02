package com.demo.bbq.business.invoice.application.service.impl;

import com.demo.bbq.business.invoice.application.dto.proformainvoice.response.ProformaInvoiceResponse;
import com.demo.bbq.business.invoice.application.service.InvoicePaymentService;
import com.demo.bbq.business.invoice.application.service.ProformaInvoiceService;
import com.demo.bbq.business.invoice.domain.exception.InvoiceException;
import com.demo.bbq.business.invoice.application.dto.invoicepayment.request.PaymentRequest;
import com.demo.bbq.business.invoice.application.events.producer.InvoiceProducer;
import com.demo.bbq.business.invoice.application.mapper.InvoiceMapper;
import com.demo.bbq.business.invoice.domain.repository.database.InvoiceRepositoryHelper;
import io.reactivex.rxjava3.core.Completable;
import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class InvoicePaymentServiceImpl implements InvoicePaymentService {
  private final ProformaInvoiceService proformaInvoiceService;
  private final InvoiceRepositoryHelper repositoryHelper;
  private final InvoiceProducer invoiceProducer;
  private final InvoiceMapper invoiceMapper;

  @Override
  public Completable sendToPay(PaymentRequest paymentRequest) {
    AtomicReference<BigDecimal> totalAmount = new AtomicReference<>();

    return proformaInvoiceService.generateProformaInvoice(paymentRequest.getProductList())
        .doOnSuccess(validateProforma::accept)
        .doOnSuccess(proforma -> totalAmount.set(proforma.getTotal()))
        .map(invoice -> invoiceMapper.toEntity(invoice, paymentRequest.getCustomer(), paymentRequest.getPayment().getMethod()))
        .map(repositoryHelper::buildEntity)
        .map(invoice -> invoiceMapper.invoiceToPayment(invoice, totalAmount.get()))
        .doOnSuccess(invoiceProducer::sendMessage)
        .ignoreElement();
  }

  private static final Consumer<ProformaInvoiceResponse> validateProforma = proforma -> {
    if(proforma.getTotal().compareTo(BigDecimal.ZERO) == 0) {
      throw InvoiceException.ERROR0000.buildException();
    }
  };

}
