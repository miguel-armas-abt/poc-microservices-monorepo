package com.demo.bbq.business.invoice.domain.repository.database.product;

import com.demo.bbq.business.invoice.domain.repository.database.product.entity.ProductEntity;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<ProductEntity, Long> {
}
