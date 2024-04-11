package com.demo.bbq.business.tableplacement.application.dto.tableplacement.request;

import lombok.*;
import java.io.Serializable;
import java.util.List;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TablePlacementRequestDTO implements Serializable {

  private List<MenuOrderRequestDTO> menuOrderList;
  private Integer tableNumber;

}
