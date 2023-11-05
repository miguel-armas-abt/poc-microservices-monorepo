package com.demo.bbq.business.invoice.infrastructure.broker.consumer;

import com.demo.bbq.business.invoice.domain.exception.InvoiceException;
import com.demo.bbq.business.invoice.domain.model.response.PaidTransaction;
import com.demo.bbq.business.invoice.infrastructure.repository.database.InvoiceRepository;
import java.math.BigDecimal;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentConsumer {

  private final InvoiceRepository invoiceRepository;

  @KafkaListener(topics = "${kafka-broker.topic.payment}")
  public void listen(Message<String> message) {
    String payload = message.getPayload();
    log.info(payload);
    PaidTransaction paidTransaction = new Gson().fromJson(payload, PaidTransaction.class);

    invoiceRepository.findById(paidTransaction.getInvoiceId())
        .map(invoice -> {
          BigDecimal actualPendingAmount = invoice.getPendingAmount();
          BigDecimal subtractedAmount = actualPendingAmount.subtract(paidTransaction.getPaidAmount());
          if (subtractedAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw InvoiceException.ERROR0004.buildException();
          }
          invoice.setPendingAmount(subtractedAmount);
          return invoiceRepository.save(invoice);
        });

    log.error(payload); //if the process wasn't successful, then send a notification to resend the payment
  }
}
