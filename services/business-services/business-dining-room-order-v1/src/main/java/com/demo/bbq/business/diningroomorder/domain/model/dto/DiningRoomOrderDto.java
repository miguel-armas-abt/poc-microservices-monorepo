package com.demo.bbq.business.diningroomorder.domain.model.dto;

import com.demo.bbq.business.diningroomorder.infrastructure.documentation.data.DiningRoomOrderExample;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.io.Serializable;
import java.util.List;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DiningRoomOrderDto implements Serializable{

  private List<MenuOrderDto> menuOrderList;

  @Schema(example = DiningRoomOrderExample.TABLE_NUMBER)
  private Integer tableNumber;

  @Schema(example = DiningRoomOrderExample.DINING_ROOM_ORDER_ID)
  private Long id;
}
