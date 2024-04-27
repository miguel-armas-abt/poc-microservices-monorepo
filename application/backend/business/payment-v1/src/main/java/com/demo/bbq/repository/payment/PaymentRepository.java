package com.demo.bbq.repository.payment;

import com.demo.bbq.repository.payment.entity.PaymentEntity;
import org.springframework.data.repository.CrudRepository;

public interface PaymentRepository extends CrudRepository<PaymentEntity, Long> {
}
