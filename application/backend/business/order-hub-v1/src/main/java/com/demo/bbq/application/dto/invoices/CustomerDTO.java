package com.demo.bbq.application.dto.invoices;

import java.io.Serializable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDTO implements Serializable {

  @NotNull
  @NotEmpty
  private String documentType;

  @NotNull
  @NotEmpty
  private String documentNumber;
}
