package com.demo.bbq.business.invoice.infrastructure.resource.rest;

import com.demo.bbq.business.invoice.application.service.InvoicePaymentService;
import com.demo.bbq.business.invoice.application.service.ProformaInvoiceService;
import com.demo.bbq.business.invoice.domain.model.request.PaymentRequest;
import com.demo.bbq.business.invoice.domain.model.request.ProductRequest;
import com.demo.bbq.business.invoice.domain.model.response.ProformaInvoice;
import com.demo.bbq.support.logstash.Markers;
import io.reactivex.Completable;
import io.reactivex.Single;
import java.util.List;
import java.util.function.Consumer;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/bbq/business/invoice/v1")
public class InvoiceRestServiceImpl implements InvoiceRestService{

  private final ProformaInvoiceService proformaInvoiceService;
  private final InvoicePaymentService invoicePaymentService;

  @PostMapping(value = "/proformas", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
  public Single<ProformaInvoice> generateProforma(HttpServletRequest servletRequest,
                                                  @Valid @RequestBody List<ProductRequest> productList) {
    logRequest.accept(servletRequest);
    return proformaInvoiceService.generateProformaInvoice(productList);
  }

  @PostMapping("/payments")
  public Completable sendToPay(HttpServletRequest servletRequest, HttpServletResponse servletResponse,
                               @Valid @RequestBody PaymentRequest paymentRequest) {
    logRequest.accept(servletRequest);
    return invoicePaymentService.sendToPay(paymentRequest)
        .doOnComplete(() -> servletResponse.setStatus(201));
  }

  private final static Consumer<HttpServletRequest> logRequest = servletRequest->
      log.info(Markers.SENSITIVE_JSON, "{}", servletRequest.getMethod() + ": " + servletRequest.getRequestURI());

}
