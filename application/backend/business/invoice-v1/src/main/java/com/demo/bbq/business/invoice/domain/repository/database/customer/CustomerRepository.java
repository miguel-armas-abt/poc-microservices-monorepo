package com.demo.bbq.business.invoice.domain.repository.database.customer;

import com.demo.bbq.business.invoice.domain.repository.database.customer.entity.CustomerEntity;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<CustomerEntity, Long> {

  Optional<CustomerEntity> findByDocumentNumber(String documentNumber);
}
