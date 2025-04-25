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
    Flux<InvoiceResponseDto> response = paramValidator.validateAndGet(extractHeadersAsMap(serverRequest), DefaultHeaders.class)
        .flatMap(headers -> paramValidator.validateAndGet(extractQueryParamsAsMap(serverRequest), DocumentNumberParam.class))
        .flatMapMany(documentNumberParam -> invoiceSearchService.findInvoicesByCustomer(documentNumberParam.getDocumentNumber(), documentNumberParam.getDocumentType()));

    return build(serverRequest.headers(), response);
  }

  private static Mono<ServerResponse> build(ServerRequest.Headers requestHeaders,
                                            Flux<InvoiceResponseDto> streamResponse) {
    return ServerResponse.ok()
        .headers(headers -> RestServerUtils.buildResponseHeaders(requestHeaders).accept(headers))
        .contentType(MediaType.APPLICATION_NDJSON)
        .body(BodyInserters.fromPublisher(streamResponse, InvoiceResponseDto.class));
  }
}
