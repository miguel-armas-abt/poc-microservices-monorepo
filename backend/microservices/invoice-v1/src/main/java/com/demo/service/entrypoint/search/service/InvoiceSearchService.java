package com.demo.service.entrypoint.search.service;

import com.demo.service.entrypoint.search.dto.response.InvoiceResponseDto;
import reactor.core.publisher.Flux;

public interface InvoiceSearchService {

  Flux<InvoiceResponseDto> findInvoicesByCustomer(String documentNumber, String documentType);
}
