package com.demo.bbq.application.events.consumer;

import com.demo.bbq.application.events.consumer.message.PaidTransactionMessage;
import com.demo.bbq.repository.invoice.InvoiceRepository;
import com.demo.bbq.repository.invoice.entity.PaymentStatus;
import com.demo.bbq.utils.errors.exceptions.BusinessException;
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
    log.info("listening message: " + payload);
    PaidTransactionMessage paidTransaction = new Gson().fromJson(payload, PaidTransactionMessage.class);

    invoiceRepository.findById(paidTransaction.getInvoiceId())
        .map(invoice -> {
          invoice.setPaymentStatus(PaymentStatus.COMPLETED);
          return invoiceRepository.save(invoice);
        })
        .orElseThrow(() -> new BusinessException("PaymentStatusCouldNotBeUpdated"));

  }
}
