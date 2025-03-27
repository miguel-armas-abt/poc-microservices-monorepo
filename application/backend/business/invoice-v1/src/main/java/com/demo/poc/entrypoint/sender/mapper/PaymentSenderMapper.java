package com.demo.poc.entrypoint.sender.mapper;

import com.demo.poc.entrypoint.calculator.dto.response.InvoiceResponseDTO;
import com.demo.poc.entrypoint.sender.dto.CustomerDTO;
import com.demo.poc.entrypoint.sender.event.message.PaymentOrderMessage;
import com.demo.poc.entrypoint.sender.repository.invoice.entity.InvoiceEntity;
import com.demo.poc.entrypoint.sender.repository.invoice.entity.PaymentMethod;
import java.math.BigDecimal;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentSenderMapper {

  @Mapping(target = "customerEntity", source = "customer")
  @Mapping(target = "paymentMethod", expression = "java(getPaymentMethod(paymentMethod))")
  InvoiceEntity toEntity(InvoiceResponseDTO invoice, CustomerDTO customer, String paymentMethod);

  @Mapping(target = "paymentMethod", expression = "java(invoiceEntity.getPaymentMethod().name())")
  @Mapping(target = "invoiceId", source = "invoiceEntity.id")
  PaymentOrderMessage toMessage(InvoiceEntity invoiceEntity, BigDecimal totalAmount);

  default PaymentMethod getPaymentMethod(String paymentMethod) {
    return PaymentMethod.valueOf(paymentMethod);
  }
}
