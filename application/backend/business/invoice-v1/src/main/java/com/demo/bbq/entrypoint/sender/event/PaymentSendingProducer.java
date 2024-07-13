package com.demo.bbq.entrypoint.sender.event;

import com.demo.bbq.entrypoint.sender.event.message.PaymentSendingMessage;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentSendingProducer {

  private final KafkaTemplate<String, String> kafkaTemplate;

  @Value("${kafka-broker.topic.invoice}")
  private String invoiceTopic;

  public void sendMessage(PaymentSendingMessage message) {
    String jsonMessage = new Gson().toJson(message);
    log.info("sending message: " + jsonMessage);
    kafkaTemplate.send(invoiceTopic, jsonMessage);
  }
}
