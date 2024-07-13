package com.demo.bbq.entrypoint.invoice.mapper;

import com.demo.bbq.entrypoint.invoice.dto.PaymentSendRequestDTO;
import com.demo.bbq.entrypoint.invoice.repository.wrapper.request.PaymentSendRequestWrapper;
import com.demo.bbq.entrypoint.invoice.repository.wrapper.request.ProductRequestWrapper;
import com.demo.bbq.entrypoint.menu.repository.wrapper.response.MenuOptionResponseWrapper;
import com.demo.bbq.entrypoint.tableorder.repository.wrapper.MenuOrderWrapper;
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
