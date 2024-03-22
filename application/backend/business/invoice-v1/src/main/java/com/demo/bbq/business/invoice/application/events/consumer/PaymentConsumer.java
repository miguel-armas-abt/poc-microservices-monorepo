package com.demo.bbq.business.invoice.application.events.consumer;

import com.demo.bbq.business.invoice.domain.exception.InvoiceException;
import com.demo.bbq.business.invoice.application.dto.response.PaidTransaction;
import com.demo.bbq.business.invoice.domain.repository.database.InvoiceRepository;
import com.demo.bbq.business.invoice.domain.repository.database.catalog.PaymentStatus;
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
          invoice.setPaymentStatus(PaymentStatus.COMPLETED);
          return invoiceRepository.save(invoice);
        })
        .orElseThrow(InvoiceException.ERROR0001::buildException);

  }
}
