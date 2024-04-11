package com.demo.bbq.business.payment.application.mapper;

import com.demo.bbq.business.payment.application.events.consumer.message.PaymentMessage;
import com.demo.bbq.business.payment.domain.repository.payment.entity.PaymentEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

  PaymentEntity toEntity(PaymentMessage payment);
}
