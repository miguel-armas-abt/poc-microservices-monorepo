package com.demo.poc.entrypoint.calculator.service;

import com.demo.poc.entrypoint.calculator.dto.request.ProductRequestDto;
import com.demo.poc.entrypoint.calculator.dto.response.InvoiceResponseDto;
import java.util.Map;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CalculatorService {

  Mono<InvoiceResponseDto> calculateInvoice(Map<String, String> headers, Flux<ProductRequestDto> products);

}
