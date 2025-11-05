package com.demo.service.entrypoint.payment.mapper;

import com.demo.service.entrypoint.payment.event.pay.message.PaymentInboundMessage;
import com.demo.service.entrypoint.payment.repository.payment.entity.PaymentEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

  PaymentEntity toEntity(PaymentInboundMessage payment);
}
