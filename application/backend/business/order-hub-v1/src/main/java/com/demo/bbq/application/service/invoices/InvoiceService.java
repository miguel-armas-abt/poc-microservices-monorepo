package com.demo.bbq.application.service.invoices;

import com.demo.bbq.application.dto.invoices.InvoicePaymentRequestDTO;
import com.demo.bbq.repository.invoice.wrapper.request.ProductRequestWrapper;
import com.demo.bbq.repository.invoice.wrapper.response.ProformaInvoiceResponseWrapper;
import java.util.List;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

public interface InvoiceService {

  Mono<ProformaInvoiceResponseWrapper> generateProforma(ServerRequest serverRequest, List<ProductRequestWrapper> productList);

  Mono<Void> sendToPay(ServerRequest serverRequest, InvoicePaymentRequestDTO invoicePaymentRequestDTO);
}
