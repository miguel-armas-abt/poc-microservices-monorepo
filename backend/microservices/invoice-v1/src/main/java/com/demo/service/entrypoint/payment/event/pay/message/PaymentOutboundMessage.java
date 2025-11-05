package com.demo.service.entrypoint.payment.event.pay.message;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentOutboundMessage implements Serializable {

  BigDecimal totalAmount;
  String paymentMethod;
  Long invoiceId;

}
