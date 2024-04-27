package com.demo.bbq.application.service.impl;

import com.demo.bbq.application.dto.proformainvoice.response.ProformaInvoiceResponseDTO;
import com.demo.bbq.application.service.InvoicePaymentService;
import com.demo.bbq.application.service.ProformaInvoiceService;
import com.demo.bbq.application.dto.invoicepayment.request.PaymentRequestDTO;
import com.demo.bbq.application.events.producer.InvoiceProducer;
import com.demo.bbq.application.mapper.InvoiceMapper;
import com.demo.bbq.repository.invoice.InvoiceRepositoryHelper;
import com.demo.bbq.utils.errors.exceptions.BusinessException;
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
  public Completable sendToPay(PaymentRequestDTO paymentRequest) {
    AtomicReference<BigDecimal> totalAmount = new AtomicReference<>();

    return proformaInvoiceService.generateProformaInvoice(paymentRequest.getProductList())
        .doOnSuccess(validateProforma::accept)
        .doOnSuccess(proforma -> totalAmount.set(proforma.getTotal()))
        .map(invoice -> invoiceMapper.toEntity(invoice, paymentRequest.getCustomer(), paymentRequest.getPayment().getMethod()))
        .map(repositoryHelper::buildEntity)
        .map(invoice -> invoiceMapper.toMessage(invoice, totalAmount.get()))
        .doOnSuccess(invoiceProducer::sendMessage)
        .ignoreElement();
  }

  private static final Consumer<ProformaInvoiceResponseDTO> validateProforma = proforma -> {
    if(proforma.getTotal().compareTo(BigDecimal.ZERO) == 0) {
      throw new BusinessException("TotalAmountLessThanZero");
    }
  };

}
