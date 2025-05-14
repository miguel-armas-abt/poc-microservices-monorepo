package com.demo.poc.entrypoint.invoice.service;

import com.demo.poc.entrypoint.invoice.dto.PaymentSendRequestDto;
import com.demo.poc.entrypoint.invoice.mapper.InvoiceMapper;
import com.demo.poc.entrypoint.invoice.repository.InvoiceRepository;
import com.demo.poc.entrypoint.invoice.repository.wrapper.request.PaymentSendRequestWrapper;
import com.demo.poc.entrypoint.invoice.repository.wrapper.request.ProductRequestWrapper;
import com.demo.poc.entrypoint.invoice.repository.wrapper.response.InvoiceResponseWrapper;
import com.demo.poc.entrypoint.menu.repository.MenuRepository;
import com.demo.poc.entrypoint.tableorder.repository.TableOrderRepository;
import com.demo.poc.entrypoint.tableorder.repository.wrapper.TableOrderResponseWrapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {

  private final InvoiceRepository invoiceRepository;
  private final TableOrderRepository tableOrderRepository;
  private final MenuRepository menuRepository;
  private final InvoiceMapper invoiceMapper;

  @Override
  public Mono<InvoiceResponseWrapper> calculateInvoice(Map<String, String> headers, List<ProductRequestWrapper> productList) {
    return invoiceRepository.generateProforma(headers, productList);
  }

  @Override
  public Mono<Void> sendToPay(Map<String, String> headers, PaymentSendRequestDto paymentSendRequest) {
    int tableNumber = paymentSendRequest.getTableNumber();
    PaymentSendRequestWrapper paymentRequest = invoiceMapper.toPaymentRequest(paymentSendRequest);
    paymentRequest.setProductList(new ArrayList<>());

    return tableOrderRepository.findByTableNumber(headers, tableNumber)
        .flatMapIterable(TableOrderResponseWrapper::getMenuOrderList)
        .flatMap(menuOrder -> menuRepository
            .findByProductCode(headers, menuOrder.getProductCode())
            .map(menuOption -> invoiceMapper.toProduct(menuOrder, menuOption))
        )
        .reduce(paymentRequest, (currentPaymentSendRequest, product) -> {
          currentPaymentSendRequest.getProductList().add(product);
          return currentPaymentSendRequest;
        })
        .flatMap(currentPaymentRequest -> invoiceRepository.sendToPay(headers, currentPaymentRequest))
        .then(tableOrderRepository.cleanTable(headers, tableNumber));
  }
}