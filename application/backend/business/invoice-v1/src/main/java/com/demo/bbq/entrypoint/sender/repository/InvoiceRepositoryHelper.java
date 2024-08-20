package com.demo.bbq.entrypoint.sender.repository;

import com.demo.bbq.entrypoint.sender.constant.InvoiceConstant;
import com.demo.bbq.entrypoint.sender.repository.invoice.InvoiceRepository;
import com.demo.bbq.entrypoint.sender.repository.invoice.entity.PaymentStatus;
import com.demo.bbq.entrypoint.sender.repository.customer.CustomerRepository;
import com.demo.bbq.entrypoint.sender.repository.customer.entity.CustomerEntity;
import com.demo.bbq.entrypoint.sender.repository.invoice.entity.InvoiceEntity;
import com.demo.bbq.entrypoint.sender.repository.consumption.ConsumptionRepository;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InvoiceRepositoryHelper {

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
