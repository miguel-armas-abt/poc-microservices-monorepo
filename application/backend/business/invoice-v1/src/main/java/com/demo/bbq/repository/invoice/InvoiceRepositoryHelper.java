package com.demo.bbq.repository.invoice;

import com.demo.bbq.application.constant.InvoiceConstant;
import com.demo.bbq.repository.invoice.entity.PaymentStatus;
import com.demo.bbq.repository.customer.CustomerRepository;
import com.demo.bbq.repository.customer.entity.CustomerEntity;
import com.demo.bbq.repository.invoice.entity.InvoiceEntity;
import com.demo.bbq.repository.consumption.ConsumptionRepository;
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

  public InvoiceEntity buildEntity(InvoiceEntity invoice) {
    LocalDateTime currentDate = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(InvoiceConstant.FORMAT_DATE);

    invoice.setBillingDate(currentDate.format(formatter));
    invoice.setPaymentStatus(PaymentStatus.PENDING);
    invoice.setCustomerEntity(getOrCreateCustomer(invoice.getCustomerEntity()));
    consumptionRepository.saveAll(invoice.getProductList());
    return invoiceRepository.save(invoice);
  }

  private CustomerEntity getOrCreateCustomer(CustomerEntity customerEntity) {
    return customerRepository.findByDocumentNumber(customerEntity.getDocumentNumber())
        .orElseGet(() -> customerRepository.save(customerEntity));
  }
}
