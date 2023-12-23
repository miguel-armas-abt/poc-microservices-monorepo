package com.demo.bbq.business.orderhub.domain.model.invoicepayment;

import java.io.Serializable;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InvoicePaymentRequest implements Serializable {

  private Integer tableNumber;
  private Customer customer;
  private Payment payment;
}
