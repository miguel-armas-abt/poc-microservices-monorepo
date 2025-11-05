package com.demo.service.entrypoint.invoice.dto;

import java.io.Serializable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto implements Serializable {

  @NotNull
  @NotEmpty
  private String documentType;

  @NotNull
  @NotEmpty
  private String documentNumber;
}
