package com.demo.bbq.business.invoice.application.events.consumer.message;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaidTransactionMessage implements Serializable {
  private BigDecimal paidAmount;
  private Long invoiceId;
}
