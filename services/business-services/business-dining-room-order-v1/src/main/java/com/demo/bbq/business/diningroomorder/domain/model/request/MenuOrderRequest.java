package com.demo.bbq.business.diningroomorder.domain.model.request;

import com.demo.bbq.business.diningroomorder.infrastructure.documentation.data.DiningRoomOrderExample;
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MenuOrderRequest implements Serializable{

  @Schema(example = DiningRoomOrderExample.MENU_ID)
  private Long menuOptionId;

  @Schema(example = DiningRoomOrderExample.MENU_QUANTITY)
  private Integer quantity;
}
