package com.demo.bbq.business.invoice.infrastructure.mapper;

import com.demo.bbq.business.invoice.domain.model.request.InvoiceRequest;
import com.demo.bbq.business.invoice.domain.model.response.Payment;
import com.demo.bbq.business.invoice.domain.model.response.ProformaInvoice;
import com.demo.bbq.business.invoice.infrastructure.repository.database.catalog.PaymentMethod;
import com.demo.bbq.business.invoice.infrastructure.repository.database.entity.InvoiceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface InvoiceMapper {

  @Mapping(target = "customerEntity", source = "invoiceRequest.customer")
  @Mapping(target = "productEntityList", source = "proformaInvoice.menuOrderList")
  @Mapping(target = "subtotal", source = "proformaInvoice.subtotal")
  @Mapping(target = "total", source = "proformaInvoice.total")
  @Mapping(target = "igv", source = "proformaInvoice.igv")
  @Mapping(target = "paymentMethod", expression = "java(getPaymentMethod(invoiceRequest.getPaymentMethod()))")
  @Mapping(target = "paymentInstallments", source = "invoiceRequest.paymentInstallments")
  InvoiceEntity toEntity(InvoiceRequest invoiceRequest, ProformaInvoice proformaInvoice);


  @Mapping(target = "paymentMethod", expression = "java(invoiceEntity.getPaymentMethod().name())")
  @Mapping(target = "paymentStatus", expression = "java(invoiceEntity.getPaymentStatus().name())")
  @Mapping(target = "invoiceId", source = "id")
  @Mapping(target = "totalAmount", expression = "java(getAmountToPay(invoiceEntity.getPaymentInstallments(), invoiceEntity.getTotal()))")
  Payment invoiceToPayment(InvoiceEntity invoiceEntity);

  default PaymentMethod getPaymentMethod(String paymentMethod) {
    return PaymentMethod.valueOf(paymentMethod);
  }

  default BigDecimal getAmountToPay(Integer paymentInstallments, BigDecimal totalAmount) {
    if (paymentInstallments > 1) {
      return totalAmount.divide(new BigDecimal(paymentInstallments));
    }
    return totalAmount;
  }
}
