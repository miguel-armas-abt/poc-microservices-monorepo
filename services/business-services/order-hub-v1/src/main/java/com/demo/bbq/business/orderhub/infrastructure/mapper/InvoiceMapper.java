package com.demo.bbq.business.orderhub.infrastructure.mapper;

import com.demo.bbq.business.orderhub.domain.model.invoicepayment.InvoicePaymentRequest;
import com.demo.bbq.business.orderhub.infrastructure.repository.restclient.invoice.dto.request.PaymentRequest;
import com.demo.bbq.business.orderhub.infrastructure.repository.restclient.invoice.dto.request.ProductRequestDto;
import com.demo.bbq.business.orderhub.infrastructure.repository.restclient.menu.dto.MenuOptionDto;
import com.demo.bbq.business.orderhub.infrastructure.repository.restclient.tableorder.dto.MenuOrderDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InvoiceMapper {

  PaymentRequest toPaymentRequest(InvoicePaymentRequest invoicePaymentRequest);

  @Mapping(target = "description", source = "menuOption.description")
  @Mapping(target = "unitPrice", source = "menuOption.price")
  @Mapping(target = "quantity", source = "menuOrder.quantity")
  ProductRequestDto toProduct(MenuOrderDto menuOrder, MenuOptionDto menuOption);
}
