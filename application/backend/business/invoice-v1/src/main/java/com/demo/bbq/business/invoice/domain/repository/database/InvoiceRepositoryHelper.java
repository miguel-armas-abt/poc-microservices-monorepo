package com.demo.bbq.business.invoice.domain.repository.database;

import com.demo.bbq.business.invoice.application.constant.InvoiceConstant;
import com.demo.bbq.business.invoice.domain.repository.database.invoice.entity.PaymentStatus;
import com.demo.bbq.business.invoice.domain.repository.database.customer.CustomerRepository;
import com.demo.bbq.business.invoice.domain.repository.database.customer.entity.CustomerEntity;
import com.demo.bbq.business.invoice.domain.repository.database.invoice.entity.InvoiceEntity;
import com.demo.bbq.business.invoice.domain.repository.database.invoice.InvoiceRepository;
import com.demo.bbq.business.invoice.domain.repository.database.product.ProductRepository;
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
    invoice.setCustomerEntity(getOrCreateCustomer(invoice.getCustomerEntity()));
    productRepository.saveAll(invoice.getProductList());
    return invoiceRepository.save(invoice);
  }

  private CustomerEntity getOrCreateCustomer(CustomerEntity customerEntity) {
    return customerRepository.findByDocumentNumber(customerEntity.getDocumentNumber())
        .orElseGet(() -> customerRepository.save(customerEntity));
  }
}
