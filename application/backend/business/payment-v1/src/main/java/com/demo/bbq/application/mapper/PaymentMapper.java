package com.demo.bbq.application.mapper;

import com.demo.bbq.application.events.consumer.message.PaymentMessage;
import com.demo.bbq.repository.payment.entity.PaymentEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

  PaymentEntity toEntity(PaymentMessage payment);
}
