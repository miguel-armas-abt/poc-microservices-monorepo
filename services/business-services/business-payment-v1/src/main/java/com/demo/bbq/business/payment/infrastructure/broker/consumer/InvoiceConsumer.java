package com.demo.bbq.business.payment.infrastructure.broker.consumer;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.messaging.Message;

@Component
@RequiredArgsConstructor
public class InvoiceConsumer {

  @KafkaListener(topics = "${kafka-broker.topic.invoice}")
  public void listen(Message<String> message) {
    String payload = message.getPayload();
    System.out.println(payload);
  }
}
