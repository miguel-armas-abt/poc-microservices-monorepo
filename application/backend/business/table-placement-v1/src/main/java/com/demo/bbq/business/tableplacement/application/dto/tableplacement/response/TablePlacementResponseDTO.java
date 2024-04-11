package com.demo.bbq.business.tableplacement.application.dto.tableplacement.response;

import lombok.*;
import java.io.Serializable;
import java.util.List;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TablePlacementResponseDTO implements Serializable{

  private List<MenuOrderResponseDTO> menuOrderList;

  private Integer tableNumber;

  private String id;
}
