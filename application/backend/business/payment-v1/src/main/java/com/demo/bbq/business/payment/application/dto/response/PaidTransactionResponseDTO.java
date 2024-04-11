package com.demo.bbq.business.payment.application.dto.response;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaidTransactionResponseDTO implements Serializable {
  private BigDecimal paidAmount;
  private Long invoiceId;
}
