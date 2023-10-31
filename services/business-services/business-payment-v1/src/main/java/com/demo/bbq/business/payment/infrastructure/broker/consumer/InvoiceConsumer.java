package com.demo.bbq.business.payment.infrastructure.broker.consumer;

import com.demo.bbq.business.payment.domain.model.response.Payment;
import com.demo.bbq.business.payment.infrastructure.mapper.PaymentMapper;
import com.demo.bbq.business.payment.infrastructure.repository.database.PaymentRepository;
import com.demo.bbq.business.payment.infrastructure.repository.restclient.PaymentGatewayApi;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.messaging.Message;

@Slf4j
@Component
@RequiredArgsConstructor
public class InvoiceConsumer {

  private final PaymentRepository paymentRepository;
  private final PaymentMapper paymentMapper;

  private final PaymentGatewayApi paymentGatewayApi;

  @KafkaListener(topics = "${kafka-broker.topic.invoice}")
  public void listen(Message<String> message) {
    String payload = message.getPayload();
    log.info(payload);

    //send to pasarell
    //if the process wasn't successful, then send a notification to resend the payment

    Payment payment = new Gson().fromJson(payload, Payment.class);
    paymentRepository.save(paymentMapper.toEntity(payment));


  }



}
