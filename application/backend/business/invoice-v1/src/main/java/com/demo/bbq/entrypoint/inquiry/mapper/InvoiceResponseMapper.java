package com.demo.bbq.entrypoint.inquiry.mapper;

import com.demo.bbq.entrypoint.inquiry.dto.response.InvoiceResponseDTO;
import com.demo.bbq.entrypoint.sender.repository.invoice.entity.InvoiceEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InvoiceResponseMapper {

  InvoiceResponseDTO toResponseDTO(InvoiceEntity invoice);
}
