package com.demo.bbq.entrypoint.tableorder.dto.params;

import com.demo.bbq.commons.toolkit.validator.params.DefaultParams;
import jakarta.validation.constraints.Positive;
import java.io.Serializable;
import lombok.*;

@Builder
@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class TableNumberParam extends DefaultParams implements Serializable {

  @Positive
  private Integer tableNumber;
}
