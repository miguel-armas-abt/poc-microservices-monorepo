package com.demo.bbq.business.invoice.infrastructure.resource.rest;

import java.util.function.Consumer;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import com.demo.bbq.business.invoice.application.service.InvoiceService;
import com.demo.bbq.business.invoice.domain.model.request.InvoiceRequest;
import com.demo.bbq.business.invoice.domain.model.response.Invoice;
import com.demo.bbq.support.logstash.Markers;
import io.reactivex.Completable;
import io.reactivex.Single;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/bbq/business/v1/invoices")
public class InvoiceApi {

  private final InvoiceService invoiceService;

  @GetMapping(produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
  public Single<Invoice> generateInvoice(
      HttpServletRequest servletRequest, @RequestParam(value = "tableNumber") Integer tableNumber) {
    logRequest.accept(servletRequest);
    return invoiceService.generateInvoice(tableNumber);
  }

  @PostMapping("/send-to-pay")
  public Completable sendToPay(HttpServletRequest servletRequest,
                                        HttpServletResponse servletResponse,
                                        @Valid @RequestBody InvoiceRequest invoiceRequest) {
    logRequest.accept(servletRequest);
    return invoiceService.sendToPay(invoiceRequest)
        .doOnComplete(() -> servletResponse.setStatus(201));
  }

  private final static Consumer<HttpServletRequest> logRequest = servletRequest->
      log.info(Markers.SENSITIVE_JSON, "{}", servletRequest.getMethod() + ": " + servletRequest.getRequestURI());

}
