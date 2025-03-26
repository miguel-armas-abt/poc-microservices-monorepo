package com.demo.bbq.entrypoint.confirmation.event;

import com.demo.bbq.commons.custom.exceptions.PaymentStatusNonUpdatedException;
import com.demo.bbq.entrypoint.confirmation.message.PaymentConfirmationMessage;
import com.demo.bbq.entrypoint.sender.repository.invoice.InvoiceRepository;
import com.demo.bbq.entrypoint.sender.repository.invoice.entity.PaymentStatus;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentConfirmationConsumer {

  private final InvoiceRepository invoiceRepository;

  @KafkaListener(topics = "${kafka-broker.topic.payment}")
  public void listen(Message<String> message) {
    String payload = message.getPayload();
    log.info("listening message: " + payload);
    PaymentConfirmationMessage paidTransaction = new Gson().fromJson(payload, PaymentConfirmationMessage.class);

    invoiceRepository.findById(paidTransaction.getInvoiceId())
        .map(invoice -> {
          invoice.setPaymentStatus(PaymentStatus.COMPLETED);
          return invoiceRepository.save(invoice);
        })
        .orElseThrow(PaymentStatusNonUpdatedException::new);

  }
}
