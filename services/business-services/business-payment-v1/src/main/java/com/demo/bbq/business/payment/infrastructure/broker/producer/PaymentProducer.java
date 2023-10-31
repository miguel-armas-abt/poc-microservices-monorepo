package com.demo.bbq.business.payment.infrastructure.broker.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentProducer {

  private final KafkaTemplate<String, String> kafkaTemplate;

  @Value("${kafka-broker.topic.payment}")
  private String invoiceTopic;

  public void sendMessage(String message) {
    kafkaTemplate.send(invoiceTopic, message);
  }
}
