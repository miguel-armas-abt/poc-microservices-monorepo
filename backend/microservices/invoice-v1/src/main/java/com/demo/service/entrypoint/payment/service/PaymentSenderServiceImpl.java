package com.demo.service.entrypoint.payment.service;

import com.demo.service.commons.exceptions.TotalAmountLessThanZeroException;
import com.demo.service.entrypoint.calculator.dto.response.InvoiceResponseDto;
import com.demo.service.entrypoint.calculator.service.CalculatorService;
import com.demo.service.entrypoint.payment.dto.PaymentSendRequestDto;
import com.demo.service.entrypoint.payment.event.pay.PaymentProducer;
import com.demo.service.entrypoint.payment.mapper.PaymentSenderMapper;
import com.demo.service.entrypoint.payment.repository.InvoiceRepositoryJoiner;
import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Service
public class PaymentSenderServiceImpl implements PaymentSenderService {
  private final CalculatorService calculatorService;
  private final InvoiceRepositoryJoiner repositoryHelper;
  private final PaymentProducer paymentProducer;
  private final PaymentSenderMapper mapper;

  @Override
  public Mono<Void> sendToPay(Map<String, String> headers,
                              PaymentSendRequestDto paymentRequest) {

    AtomicReference<BigDecimal> totalAmount = new AtomicReference<>();

    return calculatorService.calculateInvoice(headers, Flux.fromIterable(paymentRequest.getProductList()))
        .doOnSuccess(validateInvoice)
        .doOnSuccess(invoice -> totalAmount.set(invoice.getTotal()))
        .map(invoice -> mapper.toEntity(invoice, paymentRequest.getCustomer(), paymentRequest.getPayment().getMethod()))
        .map(repositoryHelper::fillFields)
        .map(invoice -> mapper.toMessage(invoice, totalAmount.get()))
        .doOnSuccess(paymentProducer::sendMessage)
        .then();
  }

  private static final Consumer<InvoiceResponseDto> validateInvoice = invoice -> {
    if(invoice.getTotal().compareTo(BigDecimal.ZERO) == 0) {
      throw new TotalAmountLessThanZeroException();
    }
  };

}
