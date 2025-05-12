package com.demo.poc.entrypoint.payment.repository.invoice;

import com.demo.poc.entrypoint.payment.repository.customer.entity.DocumentType;
import com.demo.poc.entrypoint.payment.repository.invoice.entity.InvoiceEntity;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface InvoiceRepository extends CrudRepository<InvoiceEntity, Long> {

  InvoiceEntity save(InvoiceEntity invoice);

  List<InvoiceEntity> findByCustomerEntityDocumentNumberAndCustomerEntityDocumentType(String documentNumber, DocumentType documentType);
}
