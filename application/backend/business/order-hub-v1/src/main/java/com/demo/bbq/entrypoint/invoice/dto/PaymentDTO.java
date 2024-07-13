package com.demo.bbq.entrypoint.invoice.dto;

import java.io.Serializable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDTO implements Serializable {

  @NotNull
  @NotEmpty
  private String method;
}
