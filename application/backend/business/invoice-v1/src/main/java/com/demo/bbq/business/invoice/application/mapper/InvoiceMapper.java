package com.demo.bbq.business.invoice.application.mapper;

import com.demo.bbq.business.invoice.application.dto.invoicepayment.request.Customer;
import com.demo.bbq.business.invoice.application.events.producer.message.PaymentMessage;
import com.demo.bbq.business.invoice.application.dto.proformainvoice.request.ProductRequest;
import com.demo.bbq.business.invoice.application.dto.proformainvoice.response.ProductResponse;
import com.demo.bbq.business.invoice.application.dto.proformainvoice.response.ProformaInvoiceResponse;
import com.demo.bbq.business.invoice.domain.repository.database.invoice.entity.PaymentMethod;
import com.demo.bbq.business.invoice.domain.repository.database.invoice.entity.InvoiceEntity;
import java.math.BigDecimal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InvoiceMapper {

  @Mapping(target = "customerEntity", source = "customer")
  @Mapping(target = "paymentMethod", expression = "java(getPaymentMethod(paymentMethod))")
  InvoiceEntity toEntity(ProformaInvoiceResponse proformaInvoice, Customer customer, String paymentMethod);

  @Mapping(target = "paymentMethod", expression = "java(invoiceEntity.getPaymentMethod().name())")
  @Mapping(target = "invoiceId", source = "invoiceEntity.id")
  PaymentMessage invoiceToPayment(InvoiceEntity invoiceEntity, BigDecimal totalAmount);

  @Mapping(target = "subtotal", expression = "java(getSubtotal(request, unitPrice, discount))")
  ProductResponse toProduct(ProductRequest request, BigDecimal unitPrice, BigDecimal discount);

  default BigDecimal getSubtotal(ProductRequest request, BigDecimal unitPrice, BigDecimal discount) {
    BigDecimal subtotal = unitPrice.multiply(new BigDecimal(request.getQuantity()));
    return subtotal.subtract(subtotal.multiply(discount));
  }

  default PaymentMethod getPaymentMethod(String paymentMethod) {
    return PaymentMethod.valueOf(paymentMethod);
  }
}
