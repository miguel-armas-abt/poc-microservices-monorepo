package com.demo.bbq.business.orderhub.application.dto.invoices;

import java.io.Serializable;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InvoicePaymentRequestDTO implements Serializable {

  private Integer tableNumber;
  private CustomerDTO customerDTO;
  private PaymentDTO paymentDTO;
}
