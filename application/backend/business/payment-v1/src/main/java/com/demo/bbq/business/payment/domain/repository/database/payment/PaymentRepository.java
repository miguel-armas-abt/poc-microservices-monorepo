package com.demo.bbq.business.payment.domain.repository.database.payment;

import com.demo.bbq.business.payment.domain.repository.database.payment.entity.PaymentEntity;
import org.springframework.data.repository.CrudRepository;

public interface PaymentRepository extends CrudRepository<PaymentEntity, Long> {
}
