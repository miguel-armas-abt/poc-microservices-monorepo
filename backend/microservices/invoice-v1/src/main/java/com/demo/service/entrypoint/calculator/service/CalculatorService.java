package com.demo.service.entrypoint.calculator.service;

import com.demo.service.entrypoint.calculator.dto.request.ProductRequestDto;
import com.demo.service.entrypoint.calculator.dto.response.InvoiceResponseDto;
import java.util.Map;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CalculatorService {

  Mono<InvoiceResponseDto> calculateInvoice(Map<String, String> headers, Flux<ProductRequestDto> products);

}
