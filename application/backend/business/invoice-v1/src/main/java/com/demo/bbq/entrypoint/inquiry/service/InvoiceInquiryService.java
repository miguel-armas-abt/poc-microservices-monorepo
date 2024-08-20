package com.demo.bbq.entrypoint.inquiry.service;

import com.demo.bbq.entrypoint.inquiry.dto.response.InvoiceResponseDTO;
import reactor.core.publisher.Flux;

public interface InvoiceInquiryService {

  Flux<InvoiceResponseDTO> findInvoicesByCustomer(String documentNumber, String documentType);
}
