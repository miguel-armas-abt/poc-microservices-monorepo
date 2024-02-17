package com.demo.bbq.business.tableplacement.application.dto.tableplacement.response;

import lombok.*;
import java.io.Serializable;
import java.util.List;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TablePlacementResponse implements Serializable{

  private List<MenuOrderResponse> menuOrderList;

  private Integer tableNumber;

  private String id;
}
