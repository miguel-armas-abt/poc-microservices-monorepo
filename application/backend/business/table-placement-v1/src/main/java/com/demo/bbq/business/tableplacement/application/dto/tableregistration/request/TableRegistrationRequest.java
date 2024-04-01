package com.demo.bbq.business.tableplacement.application.dto.tableregistration.request;

import java.io.Serializable;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TableRegistrationRequest implements Serializable {

  @NotNull
  private Integer tableNumber;

  @NotNull
  private Integer capacity;
}