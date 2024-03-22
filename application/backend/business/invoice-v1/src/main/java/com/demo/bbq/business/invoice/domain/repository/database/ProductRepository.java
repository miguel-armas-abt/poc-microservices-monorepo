package com.demo.bbq.business.invoice.domain.repository.database;

import com.demo.bbq.business.invoice.domain.repository.database.entity.ProductEntity;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<ProductEntity, Long> {
}
