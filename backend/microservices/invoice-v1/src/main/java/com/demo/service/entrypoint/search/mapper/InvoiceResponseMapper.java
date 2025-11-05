package com.demo.service.entrypoint.search.mapper;

import com.demo.service.entrypoint.search.dto.response.InvoiceResponseDto;
import com.demo.service.entrypoint.payment.repository.invoice.entity.InvoiceEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InvoiceResponseMapper {

  InvoiceResponseDto toResponseDto(InvoiceEntity invoice);
}
