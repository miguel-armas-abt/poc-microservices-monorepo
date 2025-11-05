package com.demo.service.entrypoint.payment.event.pay;

import com.demo.service.entrypoint.payment.event.pay.message.PaymentInboundMessage;
import com.demo.service.entrypoint.payment.event.confirmation.PaymentConfirmationProducer;
import com.demo.service.entrypoint.payment.mapper.PaymentMapper;
import com.demo.service.entrypoint.payment.repository.payment.PaymentRepository;
import com.demo.service.entrypoint.payment.repository.processor.PaymentProcessorRepository;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.messaging.Message;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentConsumer {

  private final PaymentRepository paymentRepository;
  private final PaymentMapper paymentMapper;
  private final PaymentProcessorRepository paymentProcessorRepository;
  private final PaymentConfirmationProducer paymentConfirmationProducer;

  @KafkaListener(topics = "${kafka-broker.topic.invoice}")
  public void listen(Message<String> message) {
    String payload = message.getPayload();
    log.info("listening message: " + payload);
    PaymentInboundMessage payment = new Gson().fromJson(payload, PaymentInboundMessage.class);

//    if(paymentProcessorRepository.process(PaymentRequestWrapper.builder().amount(payment.getTotalAmount()).clientCompany("BBQ").build()).getIsSuccessfulTransaction()) {
//      paymentRepository.save(paymentMapper.toEntity(payment));
//      paymentConfirmationProducer.sendMessage(new Gson().toJson(PaymentConfirmationMessage.builder().invoiceId(payment.getInvoiceId()).paidAmount(payment.getTotalAmount())));
//    }
  }
}
