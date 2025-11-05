package com.demo.service.entrypoint.calculator.rest;

import com.demo.commons.restserver.utils.RestServerUtils;
import com.demo.commons.validations.BodyValidator;
import com.demo.commons.validations.ParamValidator;
import com.demo.commons.validations.headers.DefaultHeaders;
import com.demo.service.entrypoint.calculator.dto.request.ProductRequestDto;
import com.demo.service.entrypoint.calculator.service.CalculatorService;
import java.util.Map;

import lombok.RequiredArgsConstructor;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class CalculatorHandler {

  private final CalculatorService calculatorService;
  private final BodyValidator bodyValidator;
  private final ParamValidator paramValidator;

  public Mono<ServerResponse> calculateInvoice(ServerRequest serverRequest) {
    Flux<ProductRequestDto> products = serverRequest.bodyToFlux(ProductRequestDto.class)
        .flatMap(bodyValidator::validateAndGet);

    return paramValidator.validateHeadersAndGet(serverRequest, DefaultHeaders.class)
        .flatMap(defaultHeaders -> {
          Map<String, String> headers = defaultHeaders.getValue();
          return calculatorService.calculateInvoice(headers, products);
        })
        .flatMap(response -> ServerResponse.ok()
            .headers(httpHeaders -> RestServerUtils.buildResponseHeaders(serverRequest.headers()).accept(httpHeaders))
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(response)));
  }
}
