package com.demo.bbq.business.orderhub.application.dto.invoices;

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
