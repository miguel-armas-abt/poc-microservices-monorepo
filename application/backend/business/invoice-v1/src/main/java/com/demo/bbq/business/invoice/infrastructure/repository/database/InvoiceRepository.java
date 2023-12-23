package com.demo.bbq.business.invoice.infrastructure.repository.database;

import com.demo.bbq.business.invoice.infrastructure.repository.database.entity.InvoiceEntity;
import org.springframework.data.repository.CrudRepository;

public interface InvoiceRepository extends CrudRepository<InvoiceEntity, Long> {

  InvoiceEntity save(InvoiceEntity invoice);
}
