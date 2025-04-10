package com.demo.poc.entrypoint.invoice.service;

import com.demo.poc.entrypoint.invoice.dto.PaymentSendRequestDto;
import com.demo.poc.entrypoint.invoice.repository.wrapper.request.ProductRequestWrapper;
import com.demo.poc.entrypoint.invoice.repository.wrapper.response.InvoiceResponseWrapper;
import java.util.List;
import java.util.Map;
import reactor.core.publisher.Mono;

public interface InvoiceService {

  Mono<InvoiceResponseWrapper> calculateInvoice(Map<String, String> headers, List<ProductRequestWrapper> productList);

  Mono<Void> sendToPay(Map<String, String> headers, PaymentSendRequestDto paymentSendRequestDTO);
}
