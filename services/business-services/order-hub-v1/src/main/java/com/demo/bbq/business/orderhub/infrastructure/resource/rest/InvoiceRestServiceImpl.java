package com.demo.bbq.business.orderhub.infrastructure.resource.rest;

import com.demo.bbq.business.orderhub.infrastructure.repository.restclient.invoice.InvoiceApi;
import com.demo.bbq.business.orderhub.infrastructure.repository.restclient.invoice.dto.request.PaymentRequest;
import com.demo.bbq.business.orderhub.infrastructure.repository.restclient.invoice.dto.request.ProductRequestDto;
import com.demo.bbq.business.orderhub.infrastructure.repository.restclient.invoice.dto.response.ProformaInvoiceDto;
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

  private final InvoiceApi invoiceApi;

  @PostMapping(value = "/proforma-invoices", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
  public Single<ProformaInvoiceDto> generateProforma(HttpServletRequest servletRequest,
                                                     @Valid @RequestBody List<ProductRequestDto> productList) {
    return invoiceApi.generateProforma(productList);
  }

  @PostMapping("/invoice-payments")
  public Completable sendToPay(HttpServletRequest servletRequest, HttpServletResponse servletResponse,
                              @Valid @RequestBody PaymentRequest paymentRequest) {
    return invoiceApi.sendToPay(paymentRequest)
        .doOnSuccess(ignore -> servletResponse.setStatus(201))
        .ignoreElement();
  }
}
