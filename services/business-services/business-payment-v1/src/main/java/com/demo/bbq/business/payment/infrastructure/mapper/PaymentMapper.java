package com.demo.bbq.business.payment.infrastructure.mapper;

import com.demo.bbq.business.payment.domain.model.response.Payment;
import com.demo.bbq.business.payment.infrastructure.repository.database.entity.PaymentEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

  PaymentEntity toEntity(Payment payment);
}
