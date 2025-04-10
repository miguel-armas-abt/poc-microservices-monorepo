package com.demo.poc.entrypoint.payment.mapper;

import com.demo.poc.entrypoint.payment.event.pay.message.PaymentInboundMessage;
import com.demo.poc.entrypoint.payment.repository.payment.entity.PaymentEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

  PaymentEntity toEntity(PaymentInboundMessage payment);
}
