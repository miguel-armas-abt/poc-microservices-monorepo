package com.demo.service.entrypoint.search.rest;

import com.demo.commons.restserver.utils.RestServerUtils;
import com.demo.commons.validations.ParamValidator;
import com.demo.commons.validations.headers.DefaultHeaders;
import com.demo.service.entrypoint.search.dto.response.InvoiceResponseDto;
import com.demo.service.entrypoint.search.params.DocumentNumberParam;
import com.demo.service.entrypoint.search.service.InvoiceSearchService;
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
public class InvoiceSearchHandler {

  private final InvoiceSearchService invoiceSearchService;
  private final ParamValidator paramValidator;

  public Mono<ServerResponse> findInvoicesByCustomer(ServerRequest serverRequest) {
    Flux<InvoiceResponseDto> response = paramValidator.validateHeadersAndGet(serverRequest, DefaultHeaders.class)
        .zipWith(paramValidator.validateQueryParamsAndGet(serverRequest, DocumentNumberParam.class))
        .flatMapMany(tuple -> {
          DocumentNumberParam params = tuple.getT2().getKey();
          return invoiceSearchService.findInvoicesByCustomer(params.getDocumentNumber(), params.getDocumentType());
        });

    return ServerResponse.ok()
        .headers(httpHeaders -> RestServerUtils.buildResponseHeaders(serverRequest.headers()).accept(httpHeaders))
        .contentType(MediaType.APPLICATION_NDJSON)
        .body(BodyInserters.fromPublisher(response, InvoiceResponseDto.class));
  }
}
