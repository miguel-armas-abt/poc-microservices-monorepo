package com.demo.bbq.entrypoint.inquiry.rest;

import com.demo.bbq.commons.restserver.ServerResponseFactory;
import com.demo.bbq.commons.validations.headers.DefaultHeaders;
import com.demo.bbq.commons.validations.headers.HeaderValidator;
import com.demo.bbq.commons.validations.params.ParamValidator;
import com.demo.bbq.entrypoint.inquiry.dto.response.InvoiceResponseDTO;
import com.demo.bbq.entrypoint.inquiry.dto.params.DocumentNumberParam;
import com.demo.bbq.entrypoint.inquiry.service.InvoiceInquiryService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.demo.bbq.commons.restclient.utils.HttpHeadersFiller.extractHeadersAsMap;
import static com.demo.bbq.commons.restclient.utils.QueryParamFiller.extractQueryParamsAsMap;

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
