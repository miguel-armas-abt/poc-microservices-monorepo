package com.demo.bbq.business.invoice.domain.repository.database.invoice;

import com.demo.bbq.business.invoice.domain.repository.database.invoice.entity.InvoiceEntity;
import org.springframework.data.repository.CrudRepository;

public interface InvoiceRepository extends CrudRepository<InvoiceEntity, Long> {

  InvoiceEntity save(InvoiceEntity invoice);
}
