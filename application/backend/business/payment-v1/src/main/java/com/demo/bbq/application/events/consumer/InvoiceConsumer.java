package com.demo.bbq.application.events.consumer;

import com.demo.bbq.application.dto.response.PaidTransactionResponseDTO;
import com.demo.bbq.application.events.consumer.message.PaymentMessage;
import com.demo.bbq.application.events.producer.PaymentProducer;
import com.demo.bbq.application.mapper.PaymentMapper;
import com.demo.bbq.repository.payment.PaymentRepository;
import com.demo.bbq.repository.paymetgateway.PaymentGatewayRepository;
import com.demo.bbq.repository.paymetgateway.wrapper.request.PaymentGatewayRequestWrapper;
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
  private final PaymentGatewayRepository paymentGatewayRepository;
  private final PaymentProducer paymentProducer;

  @KafkaListener(topics = "${kafka-broker.topic.invoice}")
  public void listen(Message<String> message) {
    String payload = message.getPayload();
    log.info("listening message: " + payload);
    PaymentMessage payment = new Gson().fromJson(payload, PaymentMessage.class);

    if(paymentGatewayRepository.process(PaymentGatewayRequestWrapper.builder().amount(payment.getTotalAmount()).clientCompany("BBQ").build()).getIsSuccessfulTransaction()) {
      paymentRepository.save(paymentMapper.toEntity(payment));
      paymentProducer.sendMessage(new Gson().toJson(PaidTransactionResponseDTO.builder().invoiceId(payment.getInvoiceId()).paidAmount(payment.getTotalAmount())));
    }
  }
}
