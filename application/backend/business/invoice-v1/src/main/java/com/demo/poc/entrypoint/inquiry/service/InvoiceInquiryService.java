package com.demo.poc.entrypoint.inquiry.service;

import com.demo.poc.entrypoint.inquiry.dto.response.InvoiceResponseDTO;
import reactor.core.publisher.Flux;

public interface InvoiceInquiryService {

  Flux<InvoiceResponseDTO> findInvoicesByCustomer(String documentNumber, String documentType);
}
