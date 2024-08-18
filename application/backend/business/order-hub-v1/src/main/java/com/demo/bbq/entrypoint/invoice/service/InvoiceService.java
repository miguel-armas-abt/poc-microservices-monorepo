package com.demo.bbq.entrypoint.invoice.service;

import com.demo.bbq.entrypoint.invoice.dto.PaymentSendRequestDTO;
import com.demo.bbq.entrypoint.invoice.repository.wrapper.request.ProductRequestWrapper;
import com.demo.bbq.entrypoint.invoice.repository.wrapper.response.InvoiceResponseWrapper;
import java.util.List;
import java.util.Map;
import reactor.core.publisher.Mono;

public interface InvoiceService {

  Mono<InvoiceResponseWrapper> calculateInvoice(Map<String, String> headers, List<ProductRequestWrapper> productList);

  Mono<Void> sendToPay(Map<String, String> headers, PaymentSendRequestDTO paymentSendRequestDTO);
}
