package com.demo.service.entrypoint.payment.mapper;

import com.demo.service.entrypoint.calculator.dto.response.InvoiceResponseDto;
import com.demo.service.entrypoint.payment.dto.CustomerDto;
import com.demo.service.entrypoint.payment.event.pay.message.PaymentOutboundMessage;
import com.demo.service.entrypoint.payment.repository.invoice.entity.InvoiceEntity;
import com.demo.service.entrypoint.payment.repository.invoice.entity.PaymentMethod;
import java.math.BigDecimal;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentSenderMapper {

  @Mapping(target = "customerEntity", source = "customer")
  @Mapping(target = "paymentMethod", expression = "java(getPaymentMethod(paymentMethod))")
  InvoiceEntity toEntity(InvoiceResponseDto invoice, CustomerDto customer, String paymentMethod);

  @Mapping(target = "paymentMethod", expression = "java(invoiceEntity.getPaymentMethod().name())")
  @Mapping(target = "invoiceId", source = "invoiceEntity.id")
  PaymentOutboundMessage toMessage(InvoiceEntity invoiceEntity, BigDecimal totalAmount);

  default PaymentMethod getPaymentMethod(String paymentMethod) {
    return PaymentMethod.valueOf(paymentMethod);
  }
}
