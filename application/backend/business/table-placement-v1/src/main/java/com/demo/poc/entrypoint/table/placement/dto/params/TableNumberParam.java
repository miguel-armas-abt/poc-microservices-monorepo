package com.demo.poc.entrypoint.table.placement.dto.params;

import jakarta.validation.constraints.Positive;
import java.io.Serializable;
import lombok.*;

@Builder
@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class TableNumberParam implements Serializable {

  @Positive
  private Integer tableNumber;
}
