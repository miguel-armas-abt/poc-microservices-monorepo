package com.demo.bbq.business.orderhub.infrastructure.resource.rest.invoice;

import com.demo.bbq.business.orderhub.infrastructure.repository.restclient.invoice.dto.request.PaymentRequest;
import com.demo.bbq.business.orderhub.infrastructure.repository.restclient.invoice.dto.request.ProductRequestDto;
import com.demo.bbq.business.orderhub.infrastructure.repository.restclient.invoice.dto.response.ProformaInvoiceDto;
import io.reactivex.Completable;
import io.reactivex.Single;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface InvoiceRestService {

  Single<ProformaInvoiceDto> generateProforma(HttpServletRequest servletRequest, List<ProductRequestDto> productList);

  Completable sendToPay(HttpServletRequest servletRequest, HttpServletResponse servletResponse, PaymentRequest paymentRequest);
}
