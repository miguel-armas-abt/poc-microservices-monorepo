package com.demo.poc.entrypoint.payment.dto;

import java.io.Serializable;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDto implements Serializable {
  private String method;
}
