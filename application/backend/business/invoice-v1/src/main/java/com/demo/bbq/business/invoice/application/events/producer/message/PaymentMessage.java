package com.demo.bbq.business.invoice.application.events.producer.message;

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
