package com.demo.bbq.business.invoice.domain.repository.invoice;

import com.demo.bbq.business.invoice.domain.repository.invoice.entity.InvoiceEntity;
import org.springframework.data.repository.CrudRepository;

public interface InvoiceRepository extends CrudRepository<InvoiceEntity, Long> {

  InvoiceEntity save(InvoiceEntity invoice);
}
