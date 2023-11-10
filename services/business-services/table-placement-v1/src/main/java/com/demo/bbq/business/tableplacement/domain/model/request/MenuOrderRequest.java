package com.demo.bbq.business.tableplacement.domain.model.request;

import com.demo.bbq.business.tableplacement.infrastructure.documentation.data.TablePlacementExample;
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MenuOrderRequest implements Serializable{

  @Schema(example = TablePlacementExample.MENU_ID)
  private Long menuOptionId;

  @Schema(example = TablePlacementExample.MENU_QUANTITY)
  private Integer quantity;
}
