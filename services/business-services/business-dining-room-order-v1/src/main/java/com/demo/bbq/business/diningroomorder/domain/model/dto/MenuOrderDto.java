package com.demo.bbq.business.diningroomorder.domain.model.dto;

import com.demo.bbq.business.diningroomorder.infrastructure.documentation.data.DiningRoomOrderExample;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.io.Serializable;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MenuOrderDto implements Serializable {

  @Schema(example = DiningRoomOrderExample.MENU_ID)
  private Long menuOptionId;

  @Schema(example = DiningRoomOrderExample.MENU_QUANTITY)
  private Integer quantity;

}
