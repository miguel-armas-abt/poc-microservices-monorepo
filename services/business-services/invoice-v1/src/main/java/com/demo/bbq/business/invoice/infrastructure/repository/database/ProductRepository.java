package com.demo.bbq.business.invoice.infrastructure.repository.database;

import com.demo.bbq.business.invoice.infrastructure.repository.database.entity.ProductEntity;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<ProductEntity, Long> {
}
