package com.demo.bbq.business.tableplacement.domain.model.response;

import com.demo.bbq.business.tableplacement.infrastructure.documentation.data.TablePlacementExample;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.io.Serializable;
import java.util.List;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TableOrder implements Serializable{

  private List<MenuOrder> menuOrderList;

  @Schema(example = TablePlacementExample.TABLE_NUMBER)
  private Integer tableNumber;

  @Schema(example = TablePlacementExample.DINING_ROOM_ORDER_ID)
  private Long id;
}
