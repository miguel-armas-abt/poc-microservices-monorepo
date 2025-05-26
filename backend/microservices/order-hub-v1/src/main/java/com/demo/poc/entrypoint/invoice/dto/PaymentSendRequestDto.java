package com.demo.poc.entrypoint.invoice.dto;

import java.io.Serializable;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentSendRequestDto implements Serializable {

  @NotNull
  private Integer tableNumber;

  @NotNull
  private CustomerDto customer;

  @NotNull
  private PaymentDto payment;
}
