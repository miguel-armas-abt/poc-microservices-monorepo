package com.demo.bbq.business.invoice.application.dto.invoicepayment.request;

import java.io.Serializable;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDTO implements Serializable {
  private String method;
}
