package com.demo.bbq.business.orderhub.application.invoice.service;

import com.demo.bbq.business.orderhub.infrastructure.repository.restclient.invoice.dto.request.PaymentRequest;
import com.demo.bbq.business.orderhub.infrastructure.repository.restclient.invoice.dto.request.ProductRequestDto;
import com.demo.bbq.business.orderhub.infrastructure.repository.restclient.invoice.dto.response.ProformaInvoiceDto;
import io.reactivex.Completable;
import io.reactivex.Single;
import java.util.List;

public interface InvoiceService {

  Single<ProformaInvoiceDto> generateProforma(List<ProductRequestDto> productList);

  Completable sendToPay(PaymentRequest paymentRequest, Integer tableNumber);
}
