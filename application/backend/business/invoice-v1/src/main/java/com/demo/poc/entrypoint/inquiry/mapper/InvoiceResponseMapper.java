package com.demo.poc.entrypoint.inquiry.mapper;

import com.demo.poc.entrypoint.inquiry.dto.response.InvoiceResponseDTO;
import com.demo.poc.entrypoint.sender.repository.invoice.entity.InvoiceEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InvoiceResponseMapper {

  InvoiceResponseDTO toResponseDTO(InvoiceEntity invoice);
}
