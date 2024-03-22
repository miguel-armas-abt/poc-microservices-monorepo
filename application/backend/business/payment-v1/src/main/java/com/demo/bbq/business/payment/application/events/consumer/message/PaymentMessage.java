package com.demo.bbq.business.payment.application.events.consumer.message;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentMessage implements Serializable {

  BigDecimal totalAmount;
  String paymentMethod;
  Long invoiceId;

}
