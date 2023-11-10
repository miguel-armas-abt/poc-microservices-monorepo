package com.demo.bbq.business.invoice.domain.model.response;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaidTransaction implements Serializable {
  private BigDecimal paidAmount;
  private Long invoiceId;
}
