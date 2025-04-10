package com.demo.poc.entrypoint.payment.repository.payment;

import com.demo.poc.entrypoint.payment.repository.payment.entity.PaymentEntity;
import org.springframework.data.repository.CrudRepository;

public interface PaymentRepository extends CrudRepository<PaymentEntity, Long> {
}
