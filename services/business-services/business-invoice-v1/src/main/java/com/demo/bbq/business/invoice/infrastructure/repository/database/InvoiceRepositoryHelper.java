package com.demo.bbq.business.invoice.infrastructure.repository.database;

import com.demo.bbq.business.invoice.domain.constant.InvoiceConstant;
import com.demo.bbq.business.invoice.infrastructure.repository.database.catalog.PaymentStatus;
import com.demo.bbq.business.invoice.infrastructure.repository.database.entity.CustomerEntity;
import com.demo.bbq.business.invoice.infrastructure.repository.database.entity.InvoiceEntity;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InvoiceRepositoryHelper {

  private final InvoiceRepository invoiceRepository;

  private final ProductRepository productRepository;

  private final CustomerRepository customerRepository;

  public InvoiceEntity buildEntity(InvoiceEntity invoice) {
    LocalDateTime currentDate = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(InvoiceConstant.FORMAT_DATE);

    invoice.setBillingDate(currentDate.format(formatter));
    invoice.setPaymentStatus(PaymentStatus.PENDING);
    invoice.setCustomerEntity(validateCustomer(invoice.getCustomerEntity()));
    productRepository.saveAll(invoice.getProductList());
    return invoiceRepository.save(invoice);
  }

  private CustomerEntity validateCustomer(CustomerEntity customerEntity) {
    return customerRepository.findByDocumentNumber(customerEntity.getDocumentNumber())
        .orElseGet(() -> customerRepository.save(customerEntity));
  }
}
