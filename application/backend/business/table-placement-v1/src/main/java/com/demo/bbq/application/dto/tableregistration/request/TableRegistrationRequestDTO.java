package com.demo.bbq.application.dto.tableregistration.request;

import java.io.Serializable;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TableRegistrationRequestDTO implements Serializable {

  @NotNull
  private Integer tableNumber;

  @NotNull
  private Integer capacity;
}