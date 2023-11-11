package com.demo.bbq.business.invoice.infrastructure.mapper;

import com.demo.bbq.business.invoice.domain.model.request.Customer;
import com.demo.bbq.business.invoice.domain.model.response.Payment;
import com.demo.bbq.business.invoice.domain.model.request.ProductRequest;
import com.demo.bbq.business.invoice.domain.model.response.Product;
import com.demo.bbq.business.invoice.domain.model.response.ProformaInvoice;
import com.demo.bbq.business.invoice.infrastructure.repository.database.catalog.PaymentMethod;
import com.demo.bbq.business.invoice.infrastructure.repository.database.entity.InvoiceEntity;
import java.math.BigDecimal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InvoiceMapper {

  @Mapping(target = "customerEntity", source = "customer")
  @Mapping(target = "paymentMethod", expression = "java(getPaymentMethod(paymentMethod))")
  InvoiceEntity toEntity(ProformaInvoice proformaInvoice, Customer customer, String paymentMethod);

  @Mapping(target = "paymentMethod", expression = "java(invoiceEntity.getPaymentMethod().name())")
  @Mapping(target = "invoiceId", source = "invoiceEntity.id")
  Payment invoiceToPayment(InvoiceEntity invoiceEntity, BigDecimal totalAmount);

  @Mapping(target = "subtotal", expression = "java(getSubtotal(request))")
  Product toProduct(ProductRequest request);

  default BigDecimal getSubtotal(ProductRequest request) {
    return request.getUnitPrice().multiply(new BigDecimal(request.getQuantity()));
  }

  default PaymentMethod getPaymentMethod(String paymentMethod) {
    return PaymentMethod.valueOf(paymentMethod);
  }
}
