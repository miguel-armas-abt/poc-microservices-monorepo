package com.demo.service.entrypoint.table.placement.dto.response;

import lombok.*;
import java.io.Serializable;
import java.util.List;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PlacementResponseDto implements Serializable{

  private List<MenuOrderDto> menuOrderList;

  private Integer tableNumber;

  private String id;
}
