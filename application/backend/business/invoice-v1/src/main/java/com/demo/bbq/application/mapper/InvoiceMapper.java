package com.demo.bbq.application.mapper;

import com.demo.bbq.application.dto.invoicepayment.request.CustomerDTO;
import com.demo.bbq.application.dto.proformainvoice.request.ProductRequestDTO;
import com.demo.bbq.application.events.producer.message.PaymentMessage;
import com.demo.bbq.application.dto.proformainvoice.response.ProductResponseDTO;
import com.demo.bbq.application.dto.proformainvoice.response.ProformaInvoiceResponseDTO;
import com.demo.bbq.repository.invoice.entity.PaymentMethod;
import com.demo.bbq.repository.invoice.entity.InvoiceEntity;
import java.math.BigDecimal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InvoiceMapper {

  @Mapping(target = "customerEntity", source = "customer")
  @Mapping(target = "paymentMethod", expression = "java(getPaymentMethod(paymentMethod))")
  InvoiceEntity toEntity(ProformaInvoiceResponseDTO proformaInvoice, CustomerDTO customer, String paymentMethod);

  @Mapping(target = "paymentMethod", expression = "java(invoiceEntity.getPaymentMethod().name())")
  @Mapping(target = "invoiceId", source = "invoiceEntity.id")
  PaymentMessage toMessage(InvoiceEntity invoiceEntity, BigDecimal totalAmount);

  @Mapping(target = "subtotal", expression = "java(getSubtotal(request, unitPrice, discount))")
  ProductResponseDTO toResponseDTO(ProductRequestDTO request, BigDecimal unitPrice, BigDecimal discount);

  default BigDecimal getSubtotal(ProductRequestDTO request, BigDecimal unitPrice, BigDecimal discount) {
    BigDecimal subtotal = unitPrice.multiply(new BigDecimal(request.getQuantity()));
    return subtotal.subtract(subtotal.multiply(discount));
  }

  default PaymentMethod getPaymentMethod(String paymentMethod) {
    return PaymentMethod.valueOf(paymentMethod);
  }
}
