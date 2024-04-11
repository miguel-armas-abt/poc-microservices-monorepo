package com.demo.bbq.business.payment.domain.repository.payment;

import com.demo.bbq.business.payment.domain.repository.payment.entity.PaymentEntity;
import org.springframework.data.repository.CrudRepository;

public interface PaymentRepository extends CrudRepository<PaymentEntity, Long> {
}
