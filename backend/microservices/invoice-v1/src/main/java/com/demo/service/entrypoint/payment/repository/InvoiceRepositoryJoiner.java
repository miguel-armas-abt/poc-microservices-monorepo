package com.demo.service.entrypoint.payment.repository;

import com.demo.service.entrypoint.payment.constant.InvoiceConstant;
import com.demo.service.entrypoint.payment.repository.invoice.InvoiceRepository;
import com.demo.service.entrypoint.payment.repository.invoice.entity.PaymentStatus;
import com.demo.service.entrypoint.payment.repository.customer.CustomerRepository;
import com.demo.service.entrypoint.payment.repository.customer.entity.CustomerEntity;
import com.demo.service.entrypoint.payment.repository.invoice.entity.InvoiceEntity;
import com.demo.service.entrypoint.payment.repository.consumption.ConsumptionRepository;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InvoiceRepositoryJoiner {

  private final InvoiceRepository invoiceRepository;

  private final ConsumptionRepository consumptionRepository;

  private final CustomerRepository customerRepository;

  public InvoiceEntity fillFields(InvoiceEntity invoice) {
    LocalDateTime currentDate = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(InvoiceConstant.FORMAT_DATE);

    invoice.setBillingDate(currentDate.format(formatter));
    invoice.setPaymentStatus(PaymentStatus.PENDING);
    invoice.setCustomerEntity(getOrCreateCustomer(invoice.getCustomerEntity()));
    consumptionRepository.saveAll(invoice.getProductList());
    return invoiceRepository.save(invoice);
  }

  private CustomerEntity getOrCreateCustomer(CustomerEntity customer) {
    return customerRepository.findByDocumentNumber(customer.getDocumentNumber())
        .orElseGet(() -> customerRepository.save(customer));
  }
}
