package com.demo.bbq.entrypoint.processor.mapper;

import com.demo.bbq.entrypoint.processor.event.order.message.PaymentOrderMessage;
import com.demo.bbq.entrypoint.processor.repository.payment.entity.PaymentEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

  PaymentEntity toEntity(PaymentOrderMessage payment);
}
