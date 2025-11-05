package com.demo.service.entrypoint.invoice.rest;

import com.demo.commons.restserver.utils.RestServerUtils;
import com.demo.commons.validations.BodyValidator;
import com.demo.commons.validations.headers.DefaultHeaders;
import com.demo.commons.validations.ParamValidator;
import com.demo.service.entrypoint.invoice.dto.PaymentSendRequestDto;
import com.demo.service.entrypoint.invoice.service.InvoiceService;
import com.demo.service.entrypoint.invoice.repository.wrapper.request.ProductRequestWrapper;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class InvoiceHandler {

  private final InvoiceService invoiceService;
  private final BodyValidator bodyValidator;
  private final ParamValidator paramValidator;

  public Mono<ServerResponse> calculateInvoice(ServerRequest serverRequest) {
    Map<String, String> headers = RestServerUtils.extractHeadersAsMap(serverRequest);

    Mono<List<ProductRequestWrapper>> productsMono = serverRequest.bodyToFlux(ProductRequestWrapper.class)
        .flatMap(bodyValidator::validateAndGet)
        .collectList();

    return paramValidator.validateAndGet(headers, DefaultHeaders.class)
        .zipWith(productsMono)
        .flatMap(tuple -> invoiceService.calculateInvoice(headers, tuple.getT2()))
        .flatMap(response -> ServerResponse.ok()
            .headers(httpHeaders -> RestServerUtils.buildResponseHeaders(serverRequest.headers()).accept(httpHeaders))
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(response)));
  }

  public Mono<ServerResponse> sendToPay(ServerRequest serverRequest) {
    Map<String, String> headers = RestServerUtils.extractHeadersAsMap(serverRequest);

    Mono<PaymentSendRequestDto> paymentRequest = serverRequest.bodyToMono(PaymentSendRequestDto.class)
        .flatMap(bodyValidator::validateAndGet);

    return paramValidator.validateAndGet(headers, DefaultHeaders.class)
        .zipWith(paymentRequest)
        .flatMap(tuple -> invoiceService.sendToPay(headers, tuple.getT2()))
        .then(ServerResponse.noContent()
            .headers(httpHeaders -> RestServerUtils.buildResponseHeaders(serverRequest.headers()).accept(httpHeaders))
            .build());
  }
}