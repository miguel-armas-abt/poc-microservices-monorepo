package com.demo.poc.entrypoint.sender.repository.customer;

import com.demo.poc.entrypoint.sender.repository.customer.entity.CustomerEntity;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<CustomerEntity, Long> {

  Optional<CustomerEntity> findByDocumentNumber(String documentNumber);
}
