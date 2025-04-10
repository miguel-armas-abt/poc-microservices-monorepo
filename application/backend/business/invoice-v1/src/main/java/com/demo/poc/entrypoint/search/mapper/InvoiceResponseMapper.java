package com.demo.poc.entrypoint.search.mapper;

import com.demo.poc.entrypoint.search.dto.response.InvoiceResponseDTO;
import com.demo.poc.entrypoint.payment.repository.invoice.entity.InvoiceEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InvoiceResponseMapper {

  InvoiceResponseDTO toResponseDTO(InvoiceEntity invoice);
}
