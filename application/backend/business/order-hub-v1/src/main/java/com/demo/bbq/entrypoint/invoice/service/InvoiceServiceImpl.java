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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {

  private final InvoiceRepository invoiceRepository;
  private final TableOrderRepository tableOrderRepository;
  private final MenuRepositoryStrategy menuRepositoryStrategy;
  private final InvoiceMapper invoiceMapper;

  @Override
  public Mono<InvoiceResponseWrapper> calculateInvoice(ServerRequest serverRequest, List<ProductRequestWrapper> productList) {
    return invoiceRepository.generateProforma(serverRequest, productList);
  }

  @Override
  public Mono<Void> sendToPay(ServerRequest serverRequest, PaymentSendRequestDTO paymentSendRequest) {
    int tableNumber = paymentSendRequest.getTableNumber();
    PaymentSendRequestWrapper paymentRequest = invoiceMapper.toPaymentRequest(paymentSendRequest);
    paymentRequest.setProductList(new ArrayList<>());

    return tableOrderRepository.findByTableNumber(serverRequest, tableNumber)
        .flatMapIterable(TableOrderResponseWrapper::getMenuOrderList)
        .flatMap(menuOrder -> menuRepositoryStrategy
            .getService()
            .findByProductCode(serverRequest, menuOrder.getProductCode())
            .map(menuOption -> invoiceMapper.toProduct(menuOrder, menuOption))
        )
        .reduce(paymentRequest, (currentPaymentSendRequest, product) -> {
          currentPaymentSendRequest.getProductList().add(product);
          return currentPaymentSendRequest;
        })
        .flatMap(currentPaymentRequest -> invoiceRepository.sendToPay(serverRequest, currentPaymentRequest))
        .then(tableOrderRepository.cleanTable(serverRequest, tableNumber));
  }
}