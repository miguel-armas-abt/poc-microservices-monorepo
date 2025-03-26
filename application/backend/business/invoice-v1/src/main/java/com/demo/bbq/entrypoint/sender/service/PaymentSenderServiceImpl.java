package com.demo.bbq.entrypoint.sender.service;

import com.demo.bbq.commons.custom.exceptions.TotalAmountLessThanZeroException;
import com.demo.bbq.entrypoint.calculator.dto.response.InvoiceResponseDTO;
import com.demo.bbq.entrypoint.calculator.service.CalculatorService;
import com.demo.bbq.entrypoint.sender.dto.PaymentSendRequestDTO;
import com.demo.bbq.entrypoint.sender.event.PaymentOrderProducer;
import com.demo.bbq.entrypoint.sender.mapper.PaymentSenderMapper;
import com.demo.bbq.entrypoint.sender.repository.InvoiceRepositoryJoiner;
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
  private final PaymentOrderProducer paymentOrderProducer;
  private final PaymentSenderMapper mapper;

  @Override
  public Mono<Void> sendToPay(Map<String, String> headers,
                              PaymentSendRequestDTO paymentRequest) {

    AtomicReference<BigDecimal> totalAmount = new AtomicReference<>();

    return calculatorService.calculateInvoice(headers, Flux.fromIterable(paymentRequest.getProductList()))
        .doOnSuccess(validateInvoice)
        .doOnSuccess(invoice -> totalAmount.set(invoice.getTotal()))
        .map(invoice -> mapper.toEntity(invoice, paymentRequest.getCustomer(), paymentRequest.getPayment().getMethod()))
        .map(repositoryHelper::fillFields)
        .map(invoice -> mapper.toMessage(invoice, totalAmount.get()))
        .doOnSuccess(paymentOrderProducer::sendMessage)
        .then();
  }

  private static final Consumer<InvoiceResponseDTO> validateInvoice = invoice -> {
    if(invoice.getTotal().compareTo(BigDecimal.ZERO) == 0) {
      throw new TotalAmountLessThanZeroException();
    }
  };

}
