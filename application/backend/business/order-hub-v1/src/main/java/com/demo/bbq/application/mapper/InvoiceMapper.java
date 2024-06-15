package com.demo.bbq.application.mapper;

import com.demo.bbq.application.dto.invoices.PaymentSendRequestDTO;
import com.demo.bbq.repository.invoice.wrapper.request.PaymentSendRequestWrapper;
import com.demo.bbq.repository.invoice.wrapper.request.ProductRequestWrapper;
import com.demo.bbq.repository.menu.wrapper.response.MenuOptionResponseWrapper;
import com.demo.bbq.repository.tableorder.wrapper.MenuOrderWrapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InvoiceMapper {

  PaymentSendRequestWrapper toPaymentRequest(PaymentSendRequestDTO paymentSendRequestDTO);

  @Mapping(target = "description", source = "menuOption.description")
  @Mapping(target = "productCode", source = "menuOption.productCode")
  @Mapping(target = "quantity", source = "menuOrder.quantity")
  ProductRequestWrapper toProduct(MenuOrderWrapper menuOrder, MenuOptionResponseWrapper menuOption);
}
