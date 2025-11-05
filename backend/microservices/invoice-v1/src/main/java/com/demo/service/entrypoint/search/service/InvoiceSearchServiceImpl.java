package com.demo.service.entrypoint.search.service;

import com.demo.service.entrypoint.search.dto.response.InvoiceResponseDto;
import com.demo.service.entrypoint.search.mapper.InvoiceResponseMapper;
import com.demo.service.entrypoint.payment.repository.customer.entity.DocumentType;
import com.demo.service.entrypoint.payment.repository.invoice.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class InvoiceSearchServiceImpl implements InvoiceSearchService {

  private final InvoiceResponseMapper mapper;
  private final InvoiceRepository invoiceRepository;

  @Override
  public Flux<InvoiceResponseDto> findInvoicesByCustomer(String documentNumber, String documentType) {
    return Flux.fromIterable(invoiceRepository.findByCustomerEntityDocumentNumberAndCustomerEntityDocumentType(documentNumber, DocumentType.valueOf(documentType)))
        .map(mapper::toResponseDto);
  }
}