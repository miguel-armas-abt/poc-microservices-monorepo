package com.demo.bbq.application.service.invoices;

import com.demo.bbq.application.dto.invoices.InvoicePaymentRequestDTO;
import com.demo.bbq.application.mapper.InvoiceMapper;
import com.demo.bbq.repository.invoice.InvoiceRepositoryHelper;
import com.demo.bbq.repository.invoice.wrapper.request.PaymentRequestWrapper;
import com.demo.bbq.repository.invoice.wrapper.request.ProductRequestWrapper;
import com.demo.bbq.repository.invoice.wrapper.response.ProformaInvoiceResponseWrapper;
import com.demo.bbq.repository.menu.MenuRepositoryStrategy;
import com.demo.bbq.repository.tableorder.TableOrderRepositoryHelper;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {

  private final InvoiceRepositoryHelper invoiceRepository;
  private final TableOrderRepositoryHelper tableOrderRepository;
  private final MenuRepositoryStrategy menuRepositoryStrategy;
  private final InvoiceMapper invoiceMapper;

  @Override
  public Single<ProformaInvoiceResponseWrapper> generateProforma(HttpServletRequest httpRequest,
                                                                 List<ProductRequestWrapper> productList) {
    return invoiceRepository.generateProforma(httpRequest, productList);
  }

  @Override
  public Completable sendToPay(HttpServletRequest httpRequest,
                               InvoicePaymentRequestDTO invoicePaymentRequest) {

    List<ProductRequestWrapper> productList = tableOrderRepository
        .findByTableNumber(httpRequest, invoicePaymentRequest.getTableNumber())
        .blockingGet()
        .getMenuOrderList()
        .stream()
        .map(menuOrder -> Optional.ofNullable(menuRepositoryStrategy.getService().findByProductCode(httpRequest, menuOrder.getProductCode()).blockingGet())
            .map(menuOption -> invoiceMapper.toProduct(menuOrder, menuOption))
            .get())
        .collect(Collectors.toList());

    PaymentRequestWrapper paymentRequestWrapper = invoiceMapper.toPaymentRequest(invoicePaymentRequest);
    paymentRequestWrapper.setProductList(productList);

    return invoiceRepository.sendToPay(httpRequest, paymentRequestWrapper)
        .ignoreElement()
        .andThen(tableOrderRepository
            .cleanTable(httpRequest, invoicePaymentRequest.getTableNumber())
            .ignoreElement());
  }
}