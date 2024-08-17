package com.demo.bbq.entrypoint.processor.event.order.message;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentOrderMessage implements Serializable {

  BigDecimal totalAmount;
  String paymentMethod;
  Long invoiceId;

}
