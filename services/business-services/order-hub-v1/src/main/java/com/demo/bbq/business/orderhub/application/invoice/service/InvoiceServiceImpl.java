package com.demo.bbq.business.orderhub.application.invoice.service;

import com.demo.bbq.business.orderhub.infrastructure.repository.restclient.invoice.InvoiceApi;
import com.demo.bbq.business.orderhub.infrastructure.repository.restclient.invoice.dto.request.PaymentRequest;
import com.demo.bbq.business.orderhub.infrastructure.repository.restclient.invoice.dto.request.ProductRequestDto;
import com.demo.bbq.business.orderhub.infrastructure.repository.restclient.invoice.dto.response.ProformaInvoiceDto;
import com.demo.bbq.business.orderhub.infrastructure.repository.restclient.tableorder.TableOrderApi;
import io.reactivex.Completable;
import io.reactivex.Single;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {

  private final InvoiceApi invoiceApi;

  private final TableOrderApi tableOrderApi;

  @Override
  public Single<ProformaInvoiceDto> generateProforma(List<ProductRequestDto> productList) {
    return invoiceApi.generateProforma(productList);
  }

  @Override
  public Completable sendToPay(PaymentRequest paymentRequest, Integer tableNumber) {
    return invoiceApi.sendToPay(paymentRequest)
        .ignoreElement().andThen(tableOrderApi.cleanTable(tableNumber).ignoreElement());
  }
}
