package com.demo.poc.entrypoint.search.service;

import com.demo.poc.entrypoint.search.dto.response.InvoiceResponseDto;
import reactor.core.publisher.Flux;

public interface InvoiceSearchService {

  Flux<InvoiceResponseDto> findInvoicesByCustomer(String documentNumber, String documentType);
}
