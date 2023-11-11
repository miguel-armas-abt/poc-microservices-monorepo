package com.demo.bbq.business.orderhub.application.invoice.service;

import com.demo.bbq.business.orderhub.domain.model.invoicepayment.InvoicePaymentRequest;
import com.demo.bbq.business.orderhub.infrastructure.mapper.InvoiceMapper;
import com.demo.bbq.business.orderhub.infrastructure.repository.handler.menu.MenuRepositoryHandler;
import com.demo.bbq.business.orderhub.infrastructure.repository.restclient.invoice.InvoiceApi;
import com.demo.bbq.business.orderhub.infrastructure.repository.restclient.invoice.dto.request.PaymentRequest;
import com.demo.bbq.business.orderhub.infrastructure.repository.restclient.invoice.dto.request.ProductRequestDto;
import com.demo.bbq.business.orderhub.infrastructure.repository.restclient.invoice.dto.response.ProformaInvoiceDto;
import com.demo.bbq.business.orderhub.infrastructure.repository.restclient.tableorder.TableOrderApi;
import io.reactivex.Completable;
import io.reactivex.Single;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {

  private final InvoiceApi invoiceApi;

  private final TableOrderApi tableOrderApi;

  private final MenuRepositoryHandler menuRepositoryHandler;

  private final InvoiceMapper invoiceMapper;

  @Override
  public Single<ProformaInvoiceDto> generateProforma(List<ProductRequestDto> productList) {
    return invoiceApi.generateProforma(productList);
  }

  @Override
  public Completable sendToPay(InvoicePaymentRequest invoicePaymentRequest) {
    List<ProductRequestDto> productList = tableOrderApi.findByTableNumber(invoicePaymentRequest.getTableNumber())
        .blockingGet()
        .getMenuOrderList()
        .stream()
        .map(menuOrder -> Optional.of(menuRepositoryHandler.findById(menuOrder.getMenuOptionId()).blockingGet())
            .map(menuOption -> invoiceMapper.toProduct(menuOrder, menuOption))
            .get())
        .collect(Collectors.toList());

    PaymentRequest paymentRequest = invoiceMapper.toPaymentRequest(invoicePaymentRequest);
    paymentRequest.setProductList(productList);

    return invoiceApi.sendToPay(paymentRequest)
        .ignoreElement().andThen(tableOrderApi.cleanTable(invoicePaymentRequest.getTableNumber()).ignoreElement());
  }

}
