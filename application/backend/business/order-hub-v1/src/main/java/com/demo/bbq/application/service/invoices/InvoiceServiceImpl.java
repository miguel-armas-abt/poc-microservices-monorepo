package com.demo.bbq.application.service.invoices;

import com.demo.bbq.application.dto.invoices.InvoicePaymentRequestDTO;
import com.demo.bbq.application.mapper.InvoiceMapper;
import com.demo.bbq.repository.invoice.InvoiceRepository;
import com.demo.bbq.repository.invoice.wrapper.request.PaymentRequestWrapper;
import com.demo.bbq.repository.invoice.wrapper.request.ProductRequestWrapper;
import com.demo.bbq.repository.invoice.wrapper.response.ProformaInvoiceResponseWrapper;
import com.demo.bbq.repository.menu.MenuRepositoryStrategy;
import com.demo.bbq.repository.tableorder.TableOrderRepository;
import com.demo.bbq.repository.tableorder.wrapper.TableOrderResponseWrapper;
import com.demo.bbq.utils.toolkit.RequestValidator;
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
  public Mono<ProformaInvoiceResponseWrapper> generateProforma(ServerRequest serverRequest, List<ProductRequestWrapper> productList) {
    return invoiceRepository.generateProforma(serverRequest, productList);
  }

  @Override
  public Mono<Void> sendToPay(ServerRequest serverRequest, InvoicePaymentRequestDTO invoicePaymentRequest) {
    requestValidator.validateRequestBody(invoicePaymentRequest);

    int tableNumber = invoicePaymentRequest.getTableNumber();
    PaymentRequestWrapper paymentRequest = invoiceMapper.toPaymentRequest(invoicePaymentRequest);
    paymentRequest.setProductList(new ArrayList<>());

    return tableOrderRepository.findByTableNumber(serverRequest, tableNumber)
        .flatMapIterable(TableOrderResponseWrapper::getMenuOrderList)
        .flatMap(menuOrder -> menuRepositoryStrategy
            .getService()
            .findByProductCode(serverRequest, menuOrder.getProductCode())
            .map(menuOption -> invoiceMapper.toProduct(menuOrder, menuOption))
        )
        .reduce(paymentRequest, (currentPaymentRequest, product) -> {
          currentPaymentRequest.getProductList().add(product);
          return currentPaymentRequest;
        })
        .flatMap(currentPaymentRequest -> invoiceRepository.sendToPay(serverRequest, currentPaymentRequest))
        .then(tableOrderRepository.cleanTable(serverRequest, tableNumber));
  }
}