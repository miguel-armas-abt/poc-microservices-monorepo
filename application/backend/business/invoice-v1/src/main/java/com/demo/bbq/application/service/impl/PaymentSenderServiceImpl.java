package com.demo.bbq.application.service.impl;

import com.demo.bbq.application.dto.calculator.response.InvoiceResponseDTO;
import com.demo.bbq.application.service.PaymentSenderService;
import com.demo.bbq.application.service.CalculatorService;
import com.demo.bbq.application.dto.sender.request.PaymentSendRequestDTO;
import com.demo.bbq.application.events.producer.InvoiceProducer;
import com.demo.bbq.application.mapper.InvoiceMapper;
import com.demo.bbq.repository.invoice.InvoiceRepositoryHelper;
import com.demo.bbq.commons.errors.exceptions.BusinessException;
import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Service
public class PaymentSenderServiceImpl implements PaymentSenderService {
  private final CalculatorService calculatorService;
  private final InvoiceRepositoryHelper repositoryHelper;
  private final InvoiceProducer invoiceProducer;
  private final InvoiceMapper invoiceMapper;

  @Override
  public Mono<Void> sendToPay(ServerRequest serverRequest,
                              PaymentSendRequestDTO paymentRequest) {

    AtomicReference<BigDecimal> totalAmount = new AtomicReference<>();

    return calculatorService.calculateInvoice(serverRequest, Flux.fromIterable(paymentRequest.getProductList()))
        .doOnSuccess(validateInvoice)
        .doOnSuccess(invoice -> totalAmount.set(invoice.getTotal()))
        .map(invoice -> invoiceMapper.toEntity(invoice, paymentRequest.getCustomer(), paymentRequest.getPayment().getMethod()))
        .map(repositoryHelper::buildEntity)
        .map(invoice -> invoiceMapper.toMessage(invoice, totalAmount.get()))
        .doOnSuccess(invoiceProducer::sendMessage)
        .then();
  }

  private static final Consumer<InvoiceResponseDTO> validateInvoice = invoice -> {
    if(invoice.getTotal().compareTo(BigDecimal.ZERO) == 0) {
      throw new BusinessException("TotalAmountLessThanZero");
    }
  };

}
