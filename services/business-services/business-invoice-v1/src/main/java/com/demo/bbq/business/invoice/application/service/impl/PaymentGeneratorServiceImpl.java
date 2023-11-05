package com.demo.bbq.business.invoice.application.service.impl;

import com.demo.bbq.business.invoice.application.service.PaymentGeneratorService;
import com.demo.bbq.business.invoice.application.service.ProformaInvoiceService;
import com.demo.bbq.business.invoice.domain.constant.InvoiceConstant;
import com.demo.bbq.business.invoice.domain.exception.InvoiceException;
import com.demo.bbq.business.invoice.domain.model.request.InvoiceRequest;
import com.demo.bbq.business.invoice.infrastructure.broker.producer.InvoiceProducer;
import com.demo.bbq.business.invoice.infrastructure.mapper.InvoiceMapper;
import com.demo.bbq.business.invoice.infrastructure.repository.restclient.DiningRoomOrderApi;
import com.demo.bbq.business.invoice.infrastructure.repository.database.CustomerRepository;
import com.demo.bbq.business.invoice.infrastructure.repository.database.InvoiceRepository;
import com.demo.bbq.business.invoice.infrastructure.repository.database.ProductRepository;
import com.demo.bbq.business.invoice.infrastructure.repository.database.catalog.PaymentStatus;
import com.demo.bbq.business.invoice.infrastructure.repository.database.entity.CustomerEntity;
import com.demo.bbq.business.invoice.infrastructure.repository.database.entity.InvoiceEntity;
import com.google.gson.Gson;
import io.reactivex.Completable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class PaymentGeneratorServiceImpl implements PaymentGeneratorService {
  private final ProformaInvoiceService proformaInvoiceService;

  private final DiningRoomOrderApi diningRoomOrderApi;

  private final InvoiceRepository invoiceRepository;

  private final ProductRepository productRepository;

  private final CustomerRepository customerRepository;

  private final InvoiceProducer invoiceProducer;

  private final InvoiceMapper invoiceMapper;

  @Override
  public Completable sendToPay(InvoiceRequest invoiceRequest) {
    return proformaInvoiceService.generateProformaInvoice(invoiceRequest.getTableNumber())
        .map(proformaInvoice -> invoiceMapper.toEntity(invoiceRequest, proformaInvoice))
        .map(this::buildEntity)
        .map(invoiceRepository::save)
        .map(invoiceMapper::invoiceToPayment)
        .doOnSuccess(payment-> invoiceProducer.sendMessage(new Gson().toJson(payment)))
        .flatMap(invoice -> diningRoomOrderApi.cleanTable(invoiceRequest.getTableNumber()))
        .ignoreElement();
  }

  private InvoiceEntity buildEntity(InvoiceEntity invoice) {
    if(invoice.getTotal().compareTo(BigDecimal.ZERO) == 0) {
      throw InvoiceException.ERROR0003.buildException();
    }

    if(invoice.getPaymentInstallments() < 0) {
      throw InvoiceException.ERROR0002.buildException();
    }

    if(invoice.getPaymentInstallments() == 1) { //this logic isn't correct
      invoice.setPendingAmount(invoice.getTotal());
    }

    LocalDateTime currentDate = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(InvoiceConstant.FORMAT_DATE);

    invoice.setBillingDate(currentDate.format(formatter));
    invoice.setDueDate(buildDueDate(currentDate, formatter, invoice.getPaymentInstallments()));
    invoice.setPaymentStatus(PaymentStatus.PENDING);
    invoice.setCustomerEntity(validateCustomer(invoice.getCustomerEntity()));
    productRepository.saveAll(invoice.getProductEntityList());

    return invoice;
  };

  private static final String buildDueDate(LocalDateTime currentDate, DateTimeFormatter dateFormatter,
                                           Integer paymentInstallments) {
    return paymentInstallments == 1
        ? currentDate.format(dateFormatter)
        : currentDate.plus(Period.ofMonths(paymentInstallments)).format(dateFormatter);
  }

  private CustomerEntity validateCustomer(CustomerEntity customerEntity) {
    return customerRepository.findByDocumentNumber(customerEntity.getDocumentNumber())
        .orElseGet(() -> customerRepository.save(customerEntity));
  }
}
