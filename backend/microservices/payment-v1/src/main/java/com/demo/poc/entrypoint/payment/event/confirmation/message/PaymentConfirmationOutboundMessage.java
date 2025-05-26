package com.demo.poc.entrypoint.payment.event.confirmation.message;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentConfirmationOutboundMessage implements Serializable {
  private BigDecimal paidAmount;
  private Long invoiceId;
}
