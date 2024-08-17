package com.demo.bbq.entrypoint.processor.repository.payment;

import com.demo.bbq.entrypoint.processor.repository.payment.entity.PaymentEntity;
import org.springframework.data.repository.CrudRepository;

public interface PaymentRepository extends CrudRepository<PaymentEntity, Long> {
}
