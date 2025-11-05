package com.demo.service.entrypoint.table.creation.dto.request;

import java.io.Serializable;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TableCreationRequestDto implements Serializable {

  @NotNull
  private Integer tableNumber;

  @NotNull
  private Integer capacity;
}