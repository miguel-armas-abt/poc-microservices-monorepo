package com.demo.bbq.business.invoice.application.events.producer;

import com.demo.bbq.business.invoice.application.events.producer.message.PaymentMessage;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InvoiceProducer {

  private final KafkaTemplate<String, String> kafkaTemplate;

  @Value("${kafka-broker.topic.invoice}")
  private String invoiceTopic;

  public void sendMessage(PaymentMessage message) {
    kafkaTemplate.send(invoiceTopic, new Gson().toJson(message));
  }
}
