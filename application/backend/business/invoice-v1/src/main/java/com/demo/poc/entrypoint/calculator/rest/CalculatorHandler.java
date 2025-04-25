package com.demo.poc.entrypoint.calculator.rest;

import com.demo.poc.commons.core.restserver.RestServerUtils;
import com.demo.poc.commons.core.validations.BodyValidator;
import com.demo.poc.commons.core.validations.headers.DefaultHeaders;
import com.demo.poc.commons.core.validations.ParamValidator;
import com.demo.poc.entrypoint.calculator.dto.request.ProductRequestDto;
import com.demo.poc.entrypoint.calculator.dto.response.InvoiceResponseDto;
import com.demo.poc.entrypoint.calculator.service.CalculatorService;
import java.util.Map;

import lombok.RequiredArgsConstructor;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.demo.poc.commons.core.restclient.utils.HttpHeadersFiller.extractHeadersAsMap;

@Component
@RequiredArgsConstructor
public class CalculatorHandler {

  private final CalculatorService calculatorService;
  private final BodyValidator bodyValidator;
  private final ParamValidator paramValidator;

  public Mono<ServerResponse> calculateInvoice(ServerRequest serverRequest) {
    Map<String, String> headers = extractHeadersAsMap(serverRequest);
    Flux<ProductRequestDto> products = serverRequest.bodyToFlux(ProductRequestDto.class)
        .flatMap(bodyValidator::validateAndGet);

    return paramValidator.validateAndGet(headers, DefaultHeaders.class)
        .flatMap(defaultHeaders -> calculatorService.calculateInvoice(headers, products))
        .flatMap(response -> single(serverRequest.headers(), response));
  }

  private static Mono<ServerResponse> single(ServerRequest.Headers requestHeaders, InvoiceResponseDto response) {
    return ServerResponse.ok()
        .headers(headers -> RestServerUtils.buildResponseHeaders(requestHeaders).accept(headers))
        .contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(response));
  }
}
