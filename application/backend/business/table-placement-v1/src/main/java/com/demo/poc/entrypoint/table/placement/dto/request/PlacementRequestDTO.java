package com.demo.poc.entrypoint.table.placement.dto.request;

import lombok.*;
import java.io.Serializable;
import java.util.List;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PlacementRequestDTO implements Serializable {

  private List<MenuOrderDTO> menuOrderList;
  private Integer tableNumber;

}
