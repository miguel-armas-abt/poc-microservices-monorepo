package com.demo.bbq.business.invoice.application.service.impl;

import com.demo.bbq.business.invoice.application.service.PaymentGeneratorService;
import com.demo.bbq.business.invoice.application.service.ProformaInvoiceService;
import com.demo.bbq.business.invoice.domain.model.request.InvoiceRequest;
import com.demo.bbq.business.invoice.infrastructure.broker.producer.InvoiceProducer;
import com.demo.bbq.business.invoice.infrastructure.repository.restclient.DiningRoomOrderApi;
import com.google.gson.Gson;
import io.reactivex.Completable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class PaymentGeneratorServiceImpl implements PaymentGeneratorService {

  private final ProformaInvoiceService proformaInvoiceService;
  private final InvoiceProducer invoiceProducer;

  private final DiningRoomOrderApi diningRoomOrderApi;

  @Override
  public Completable sendToPay(InvoiceRequest invoiceRequest) {
    return proformaInvoiceService.generateProformaInvoice(invoiceRequest.getTableNumber())
        .map(invoice -> new Gson().toJson(invoice))
        .doOnSuccess(invoiceProducer::sendMessage)
        .flatMap(jsonInvoice -> diningRoomOrderApi.cleanTable(invoiceRequest.getTableNumber()))
        .ignoreElement();
  }
}
