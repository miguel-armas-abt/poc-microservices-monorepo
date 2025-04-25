package com.demo.poc.entrypoint.search.rest;

import com.demo.poc.commons.core.restserver.RestServerUtils;
import com.demo.poc.commons.core.validations.headers.DefaultHeaders;
import com.demo.poc.commons.core.validations.ParamValidator;
import com.demo.poc.entrypoint.search.dto.response.InvoiceResponseDto;
import com.demo.poc.entrypoint.search.params.DocumentNumberParam;
import com.demo.poc.entrypoint.search.service.InvoiceSearchService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.demo.poc.commons.core.restclient.utils.HttpHeadersFiller.extractHeadersAsMap;
import static com.demo.poc.commons.core.restclient.utils.QueryParamFiller.extractQueryParamsAsMap;

@Component
@RequiredArgsConstructor
public class InvoiceSearchHandler {

  private final InvoiceSearchService invoiceSearchService;
  private final ParamValidator paramValidator;

  public Mono<ServerResponse> findInvoicesByCustomer(ServerRequest serverRequest) {
    Mono<DocumentNumberParam> documentNumberParamMono = paramValidator.validateAndGet(extractQueryParamsAsMap(serverRequest), DocumentNumberParam.class);

    Flux<InvoiceResponseDto> response = paramValidator.validateAndGet(extractHeadersAsMap(serverRequest), DefaultHeaders.class)
        .zipWith(documentNumberParamMono)
        .flatMapMany(tuple -> invoiceSearchService.findInvoicesByCustomer(tuple.getT2().getDocumentNumber(), tuple.getT2().getDocumentType()));

    return ServerResponse.ok()
        .headers(httpHeaders -> RestServerUtils.buildResponseHeaders(serverRequest.headers()).accept(httpHeaders))
        .contentType(MediaType.APPLICATION_NDJSON)
        .body(BodyInserters.fromPublisher(response, InvoiceResponseDto.class))
  }
}
