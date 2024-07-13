package com.demo.bbq.entrypoint.calculator.service;

import com.demo.bbq.entrypoint.calculator.dto.request.ProductRequestDTO;
import com.demo.bbq.entrypoint.calculator.dto.response.InvoiceResponseDTO;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CalculatorService {

  Mono<InvoiceResponseDTO> calculateInvoice(ServerRequest serverRequest, Flux<ProductRequestDTO> products);

}
