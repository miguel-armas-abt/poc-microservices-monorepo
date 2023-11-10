package com.demo.bbq.business.payment.infrastructure.repository.database;

import com.demo.bbq.business.payment.infrastructure.repository.database.entity.PaymentEntity;
import org.springframework.data.repository.CrudRepository;

public interface PaymentRepository extends CrudRepository<PaymentEntity, Long> {
}
