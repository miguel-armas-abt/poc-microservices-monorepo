package com.demo.bbq.business.orderhub.application.mapper;

import com.demo.bbq.business.orderhub.application.dto.invoices.InvoicePaymentRequestDTO;
import com.demo.bbq.business.orderhub.domain.repository.invoice.wrapper.request.PaymentRequestWrapper;
import com.demo.bbq.business.orderhub.domain.repository.invoice.wrapper.request.ProductRequestWrapper;
import com.demo.bbq.business.orderhub.domain.repository.menu.wrapper.response.MenuOptionResponseWrapper;
import com.demo.bbq.business.orderhub.domain.repository.tableorder.wrapper.MenuOrderWrapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InvoiceMapper {

  PaymentRequestWrapper toPaymentRequest(InvoicePaymentRequestDTO invoicePaymentRequestDTO);

  @Mapping(target = "description", source = "menuOption.description")
  @Mapping(target = "productCode", source = "menuOption.productCode")
  @Mapping(target = "quantity", source = "menuOrder.quantity")
  ProductRequestWrapper toProduct(MenuOrderWrapper menuOrder, MenuOptionResponseWrapper menuOption);
}
