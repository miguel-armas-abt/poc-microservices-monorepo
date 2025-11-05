package com.demo.service.entrypoint.invoice.mapper;

import com.demo.service.entrypoint.invoice.dto.PaymentSendRequestDto;
import com.demo.service.entrypoint.invoice.repository.wrapper.request.PaymentSendRequestWrapper;
import com.demo.service.entrypoint.invoice.repository.wrapper.request.ProductRequestWrapper;
import com.demo.service.entrypoint.menu.repository.wrapper.response.MenuOptionResponseWrapper;
import com.demo.service.entrypoint.tableorder.repository.wrapper.MenuOrderWrapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InvoiceMapper {

  PaymentSendRequestWrapper toPaymentRequest(PaymentSendRequestDto paymentSendRequestDTO);

  @Mapping(target = "description", source = "menuOption.description")
  @Mapping(target = "productCode", source = "menuOption.productCode")
  @Mapping(target = "quantity", source = "menuOrder.quantity")
  ProductRequestWrapper toProduct(MenuOrderWrapper menuOrder, MenuOptionResponseWrapper menuOption);
}
