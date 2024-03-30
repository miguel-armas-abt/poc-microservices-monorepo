package com.demo.bbq.business.orderhub.infrastructure.rest;

import com.demo.bbq.business.orderhub.application.service.invoices.InvoiceService;
import com.demo.bbq.business.orderhub.application.dto.invoices.InvoicePaymentRequestDTO;
import com.demo.bbq.business.orderhub.domain.repository.invoice.wrapper.request.ProductRequestWrapper;
import com.demo.bbq.business.orderhub.domain.repository.invoice.wrapper.response.ProformaInvoiceResponseWrapper;
import io.reactivex.Completable;
import io.reactivex.Single;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InvoiceRestServiceImpl extends OrderHubRestService {

  private final InvoiceService invoiceService;

  @PostMapping(value = "/proforma-invoices", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
  public Single<ProformaInvoiceResponseWrapper> generateProforma(HttpServletRequest servletRequest,
                                                                 @Valid @RequestBody List<ProductRequestWrapper> productList) {
    return invoiceService.generateProforma(productList);
  }

  @PostMapping("/invoice-payments")
  public Completable sendToPay(HttpServletRequest servletRequest, HttpServletResponse servletResponse,
                              @Valid @RequestBody InvoicePaymentRequestDTO invoicePaymentRequestDTO) {
    return invoiceService.sendToPay(invoicePaymentRequestDTO)
        .doOnComplete(() -> servletResponse.setStatus(201));
  }
}
