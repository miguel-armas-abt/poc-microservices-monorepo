package com.demo.poc.entrypoint.invoice.rest;

import com.demo.poc.commons.core.restserver.ServerResponseBuilder;
import com.demo.poc.commons.core.validations.body.BodyValidator;
import com.demo.poc.commons.core.validations.headers.DefaultHeaders;
import com.demo.poc.commons.core.validations.headers.HeaderValidator;
import com.demo.poc.entrypoint.invoice.dto.PaymentSendRequestDto;
import com.demo.poc.entrypoint.invoice.service.InvoiceService;
import com.demo.poc.entrypoint.invoice.repository.wrapper.request.ProductRequestWrapper;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.demo.poc.commons.core.restclient.utils.HttpHeadersFiller.extractHeadersAsMap;

@Component
@RequiredArgsConstructor
public class InvoiceHandler {

  private final InvoiceService invoiceService;
  private final BodyValidator bodyValidator;
  private final HeaderValidator headerValidator;

  public Mono<ServerResponse> calculateInvoice(ServerRequest serverRequest) {
    Map<String, String> headers = extractHeadersAsMap(serverRequest);
    headerValidator.validate(headers, DefaultHeaders.class);

    return serverRequest.bodyToFlux(ProductRequestWrapper.class)
        .doOnNext(bodyValidator::validate)
        .collectList()
        .flatMap(productList -> invoiceService.calculateInvoice(headers, productList))
        .flatMap(response -> ServerResponseBuilder
            .buildMono(ServerResponse.ok(), serverRequest.headers(), response));
  }

  public Mono<ServerResponse> sendToPay(ServerRequest serverRequest) {
    Map<String, String> headers = extractHeadersAsMap(serverRequest);
    headerValidator.validate(headers, DefaultHeaders.class);

    return serverRequest
        .bodyToMono(PaymentSendRequestDto.class)
        .doOnNext(bodyValidator::validate)
        .flatMap(request -> invoiceService.sendToPay(headers, request))
        .then(ServerResponseBuilder.buildEmpty(serverRequest.headers()));
  }
}