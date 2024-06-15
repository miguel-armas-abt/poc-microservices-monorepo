package com.demo.bbq.application.service.invoices;

import com.demo.bbq.application.dto.invoices.PaymentSendRequestDTO;
import com.demo.bbq.application.mapper.InvoiceMapper;
import com.demo.bbq.repository.invoice.InvoiceRepository;
import com.demo.bbq.repository.invoice.wrapper.request.PaymentSendRequestWrapper;
import com.demo.bbq.repository.invoice.wrapper.request.ProductRequestWrapper;
import com.demo.bbq.repository.invoice.wrapper.response.InvoiceResponseWrapper;
import com.demo.bbq.repository.menu.MenuRepositoryStrategy;
import com.demo.bbq.repository.tableorder.TableOrderRepository;
import com.demo.bbq.repository.tableorder.wrapper.TableOrderResponseWrapper;
import com.demo.bbq.commons.toolkit.RequestValidator;
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
  private final RequestValidator requestValidator;

  @Override
  public Mono<InvoiceResponseWrapper> calculateInvoice(ServerRequest serverRequest, List<ProductRequestWrapper> productList) {
    return invoiceRepository.generateProforma(serverRequest, productList);
  }

  @Override
  public Mono<Void> sendToPay(ServerRequest serverRequest, PaymentSendRequestDTO paymentSendRequest) {
    requestValidator.validateRequestBody(paymentSendRequest);

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