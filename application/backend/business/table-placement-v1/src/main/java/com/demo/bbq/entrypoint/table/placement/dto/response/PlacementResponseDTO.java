package com.demo.bbq.entrypoint.table.placement.dto.response;

import lombok.*;
import java.io.Serializable;
import java.util.List;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PlacementResponseDTO implements Serializable{

  private List<MenuOrderDTO> menuOrderList;

  private Integer tableNumber;

  private String id;
}
