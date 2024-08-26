package com.demo.bbq.entrypoint.inquiry.rest;

import static com.demo.bbq.commons.toolkit.params.filler.HttpHeadersFiller.extractHeadersAsMap;
import static com.demo.bbq.commons.toolkit.params.filler.QueryParamFiller.extractQueryParamsAsMap;

import com.demo.bbq.commons.toolkit.router.ServerResponseFactory;
import com.demo.bbq.commons.toolkit.validator.headers.DefaultHeaders;
import com.demo.bbq.commons.toolkit.validator.headers.HeaderValidator;
import com.demo.bbq.commons.toolkit.validator.params.ParamValidator;
import com.demo.bbq.entrypoint.inquiry.dto.response.InvoiceResponseDTO;
import com.demo.bbq.entrypoint.inquiry.dto.params.DocumentNumberParam;
import com.demo.bbq.entrypoint.inquiry.service.InvoiceInquiryService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class InvoiceInquiryHandler {

  private final InvoiceInquiryService invoiceInquiryService;
  private final HeaderValidator headerValidator;
  private final ParamValidator paramValidator;

  public Mono<ServerResponse> findInvoicesByCustomer(ServerRequest serverRequest) {
    Map<String, String> headers = extractHeadersAsMap(serverRequest);
    headerValidator.validate(headers, DefaultHeaders.class);

    DocumentNumberParam documentNumberParam = paramValidator.validateAndRetrieve(extractQueryParamsAsMap(serverRequest), DocumentNumberParam.class);
    return ServerResponseFactory
        .buildFlux(
            ServerResponse.ok(),
            serverRequest.headers(),
            InvoiceResponseDTO.class,
            invoiceInquiryService.findInvoicesByCustomer(documentNumberParam.getDocumentNumber(), documentNumberParam.getDocumentType())
        );
  }
}
