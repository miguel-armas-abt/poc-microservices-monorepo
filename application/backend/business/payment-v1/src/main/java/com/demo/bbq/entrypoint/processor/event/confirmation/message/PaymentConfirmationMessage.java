package com.demo.bbq.entrypoint.processor.event.confirmation.message;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentConfirmationMessage implements Serializable {
  private BigDecimal paidAmount;
  private Long invoiceId;
}
