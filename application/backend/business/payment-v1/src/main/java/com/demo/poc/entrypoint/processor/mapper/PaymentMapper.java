package com.demo.poc.entrypoint.processor.mapper;

import com.demo.poc.entrypoint.processor.event.order.message.PaymentOrderMessage;
import com.demo.poc.entrypoint.processor.repository.payment.entity.PaymentEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

  PaymentEntity toEntity(PaymentOrderMessage payment);
}
