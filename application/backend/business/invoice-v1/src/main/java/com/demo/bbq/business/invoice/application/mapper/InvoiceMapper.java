package com.demo.bbq.business.invoice.application.mapper;

import com.demo.bbq.business.invoice.application.dto.request.Customer;
import com.demo.bbq.business.invoice.application.dto.response.Payment;
import com.demo.bbq.business.invoice.application.dto.request.ProductRequest;
import com.demo.bbq.business.invoice.application.dto.response.Product;
import com.demo.bbq.business.invoice.application.dto.response.ProformaInvoiceResponse;
import com.demo.bbq.business.invoice.domain.repository.database.catalog.PaymentMethod;
import com.demo.bbq.business.invoice.domain.repository.database.entity.InvoiceEntity;
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
  Payment invoiceToPayment(InvoiceEntity invoiceEntity, BigDecimal totalAmount);

  @Mapping(target = "subtotal", expression = "java(getSubtotal(request, unitPrice, discount))")
  Product toProduct(ProductRequest request, BigDecimal unitPrice, BigDecimal discount);

  default BigDecimal getSubtotal(ProductRequest request, BigDecimal unitPrice, BigDecimal discount) {
    BigDecimal subtotal = unitPrice.multiply(new BigDecimal(request.getQuantity()));
    return subtotal.subtract(subtotal.multiply(discount));
  }

  default PaymentMethod getPaymentMethod(String paymentMethod) {
    return PaymentMethod.valueOf(paymentMethod);
  }
}
