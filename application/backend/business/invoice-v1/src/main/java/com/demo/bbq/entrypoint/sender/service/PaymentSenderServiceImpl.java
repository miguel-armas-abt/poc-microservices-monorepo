package com.demo.bbq.entrypoint.sender.service;

import com.demo.bbq.entrypoint.calculator.dto.response.InvoiceResponseDTO;
import com.demo.bbq.entrypoint.calculator.service.CalculatorService;
import com.demo.bbq.entrypoint.sender.dto.PaymentSendRequestDTO;
import com.demo.bbq.entrypoint.sender.event.PaymentOrderProducer;
import com.demo.bbq.entrypoint.sender.mapper.PaymentSenderMapper;
import com.demo.bbq.entrypoint.sender.repository.InvoiceRepositoryHandler;
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
  private final InvoiceRepositoryHandler repositoryHelper;
  private final PaymentOrderProducer paymentOrderProducer;
  private final PaymentSenderMapper mapper;

  @Override
  public Mono<Void> sendToPay(ServerRequest serverRequest,
                              PaymentSendRequestDTO paymentRequest) {

    AtomicReference<BigDecimal> totalAmount = new AtomicReference<>();

    return calculatorService.calculateInvoice(serverRequest, Flux.fromIterable(paymentRequest.getProductList()))
        .doOnSuccess(validateInvoice)
        .doOnSuccess(invoice -> totalAmount.set(invoice.getTotal()))
        .map(invoice -> mapper.toEntity(invoice, paymentRequest.getCustomer(), paymentRequest.getPayment().getMethod()))
        .map(repositoryHelper::buildEntity)
        .map(invoice -> mapper.toMessage(invoice, totalAmount.get()))
        .doOnSuccess(paymentOrderProducer::sendMessage)
        .then();
  }

  private static final Consumer<InvoiceResponseDTO> validateInvoice = invoice -> {
    if(invoice.getTotal().compareTo(BigDecimal.ZERO) == 0) {
      throw new BusinessException("TotalAmountLessThanZero");
    }
  };

}
