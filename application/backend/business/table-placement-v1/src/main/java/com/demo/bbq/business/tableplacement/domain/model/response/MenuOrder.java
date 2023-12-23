package com.demo.bbq.business.tableplacement.domain.model.response;

import com.demo.bbq.business.tableplacement.infrastructure.documentation.data.TablePlacementExample;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.io.Serializable;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MenuOrder implements Serializable {

  @Schema(example = TablePlacementExample.MENU_ID)
  private String productCode;

  @Schema(example = TablePlacementExample.MENU_QUANTITY)
  private Integer quantity;

}
