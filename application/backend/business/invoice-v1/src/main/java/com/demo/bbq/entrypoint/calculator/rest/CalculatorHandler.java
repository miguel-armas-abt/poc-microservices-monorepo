package com.demo.bbq.entrypoint.calculator.rest;

import com.demo.bbq.entrypoint.calculator.dto.request.ProductRequestDTO;
import com.demo.bbq.entrypoint.calculator.service.CalculatorService;
import com.demo.bbq.commons.toolkit.router.ServerResponseBuilderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class CalculatorHandler {

  private final CalculatorService calculatorService;

  public Mono<ServerResponse> calculateInvoice(ServerRequest serverRequest) {
    return calculatorService.calculateInvoice(serverRequest, serverRequest.bodyToFlux(ProductRequestDTO.class))
        .flatMap(response -> ServerResponseBuilderUtil
            .buildMono(ServerResponse.ok(), serverRequest.headers(), response));
  }
}
