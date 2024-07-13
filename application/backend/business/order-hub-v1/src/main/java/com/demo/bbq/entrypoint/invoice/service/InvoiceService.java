package com.demo.bbq.entrypoint.invoice.service;

import com.demo.bbq.entrypoint.invoice.dto.PaymentSendRequestDTO;
import com.demo.bbq.entrypoint.invoice.repository.wrapper.request.ProductRequestWrapper;
import com.demo.bbq.entrypoint.invoice.repository.wrapper.response.InvoiceResponseWrapper;
import java.util.List;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

public interface InvoiceService {

  Mono<InvoiceResponseWrapper> calculateInvoice(ServerRequest serverRequest, List<ProductRequestWrapper> productList);

  Mono<Void> sendToPay(ServerRequest serverRequest, PaymentSendRequestDTO paymentSendRequestDTO);
}
