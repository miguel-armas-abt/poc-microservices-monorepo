package com.demo.bbq.entrypoint.invoice.service;

import com.demo.bbq.entrypoint.invoice.dto.PaymentSendRequestDTO;
import com.demo.bbq.entrypoint.invoice.mapper.InvoiceMapper;
import com.demo.bbq.entrypoint.invoice.repository.InvoiceRepository;
import com.demo.bbq.entrypoint.invoice.repository.wrapper.request.PaymentSendRequestWrapper;
import com.demo.bbq.entrypoint.invoice.repository.wrapper.request.ProductRequestWrapper;
import com.demo.bbq.entrypoint.invoice.repository.wrapper.response.InvoiceResponseWrapper;
import com.demo.bbq.entrypoint.menu.repository.MenuRepositoryStrategy;
import com.demo.bbq.entrypoint.tableorder.repository.TableOrderRepository;
import com.demo.bbq.entrypoint.tableorder.repository.wrapper.TableOrderResponseWrapper;
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
  private final MenuRepositoryStrategy menuRepositoryStrategy;
  private final InvoiceMapper invoiceMapper;

  @Override
  public Mono<InvoiceResponseWrapper> calculateInvoice(Map<String, String> headers, List<ProductRequestWrapper> productList) {
    return invoiceRepository.generateProforma(headers, productList);
  }

  @Override
  public Mono<Void> sendToPay(Map<String, String> headers, PaymentSendRequestDTO paymentSendRequest) {
    int tableNumber = paymentSendRequest.getTableNumber();
    PaymentSendRequestWrapper paymentRequest = invoiceMapper.toPaymentRequest(paymentSendRequest);
    paymentRequest.setProductList(new ArrayList<>());

    return tableOrderRepository.findByTableNumber(headers, tableNumber)
        .flatMapIterable(TableOrderResponseWrapper::getMenuOrderList)
        .flatMap(menuOrder -> menuRepositoryStrategy
            .getService()
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