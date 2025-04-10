package com.demo.poc.entrypoint.search.service;

import com.demo.poc.entrypoint.search.dto.response.InvoiceResponseDTO;
import reactor.core.publisher.Flux;

public interface InvoiceSearchService {

  Flux<InvoiceResponseDTO> findInvoicesByCustomer(String documentNumber, String documentType);
}
