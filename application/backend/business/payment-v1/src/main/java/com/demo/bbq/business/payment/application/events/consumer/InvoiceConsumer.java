package com.demo.bbq.business.payment.application.events.consumer;

import com.demo.bbq.business.payment.application.dto.response.PaidTransactionResponse;
import com.demo.bbq.business.payment.application.events.consumer.message.PaymentMessage;
import com.demo.bbq.business.payment.application.events.producer.PaymentProducer;
import com.demo.bbq.business.payment.application.mapper.PaymentMapper;
import com.demo.bbq.business.payment.domain.repository.database.payment.PaymentRepository;
import com.demo.bbq.business.payment.domain.repository.restclient.paymetgateway.PaymentGatewayApi;
import com.demo.bbq.business.payment.domain.repository.restclient.paymetgateway.wrapper.request.PaymentGatewayRequest;
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
  private final PaymentProducer paymentProducer;

  @KafkaListener(topics = "${kafka-broker.topic.invoice}")
  public void listen(Message<String> message) {
    String payload = message.getPayload();
    log.info("listening message: " + payload);
    PaymentMessage payment = new Gson().fromJson(payload, PaymentMessage.class);

    if(paymentGatewayApi.process(PaymentGatewayRequest.builder().amount(payment.getTotalAmount()).clientCompany("BBQ").build()).getIsSuccessfulTransaction()) {
      paymentRepository.save(paymentMapper.toEntity(payment));
      paymentProducer.sendMessage(new Gson().toJson(PaidTransactionResponse.builder().invoiceId(payment.getInvoiceId()).paidAmount(payment.getTotalAmount())));
    }
    log.error(payload); //if the process wasn't successful, then send a notification to resend the payment
  }
}
