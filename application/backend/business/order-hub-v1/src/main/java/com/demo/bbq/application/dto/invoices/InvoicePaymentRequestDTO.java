package com.demo.bbq.application.dto.invoices;

import java.io.Serializable;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InvoicePaymentRequestDTO implements Serializable {

  @NotNull
  private Integer tableNumber;

  @NotNull
  private CustomerDTO customer;

  @NotNull
  private PaymentDTO payment;
}
