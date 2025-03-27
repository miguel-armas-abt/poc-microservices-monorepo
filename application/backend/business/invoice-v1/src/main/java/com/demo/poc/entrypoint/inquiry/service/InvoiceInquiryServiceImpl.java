package com.demo.poc.entrypoint.inquiry.service;

import com.demo.poc.entrypoint.inquiry.dto.response.InvoiceResponseDTO;
import com.demo.poc.entrypoint.inquiry.mapper.InvoiceResponseMapper;
import com.demo.poc.entrypoint.sender.repository.customer.entity.DocumentType;
import com.demo.poc.entrypoint.sender.repository.invoice.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class InvoiceInquiryServiceImpl implements InvoiceInquiryService {

  private final InvoiceResponseMapper mapper;
  private final InvoiceRepository invoiceRepository;

  @Override
  public Flux<InvoiceResponseDTO> findInvoicesByCustomer(String documentNumber, String documentType) {
    return Flux.fromIterable(invoiceRepository.findByCustomerEntityDocumentNumberAndCustomerEntityDocumentType(documentNumber, DocumentType.valueOf(documentType)))
        .map(mapper::toResponseDTO);
  }
}