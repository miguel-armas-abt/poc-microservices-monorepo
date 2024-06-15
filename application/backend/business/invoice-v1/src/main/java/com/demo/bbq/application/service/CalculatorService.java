package com.demo.bbq.application.service;

import com.demo.bbq.application.dto.calculator.request.ProductRequestDTO;
import com.demo.bbq.application.dto.calculator.response.InvoiceResponseDTO;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CalculatorService {

  Mono<InvoiceResponseDTO> calculateInvoice(ServerRequest serverRequest, Flux<ProductRequestDTO> products);

}
