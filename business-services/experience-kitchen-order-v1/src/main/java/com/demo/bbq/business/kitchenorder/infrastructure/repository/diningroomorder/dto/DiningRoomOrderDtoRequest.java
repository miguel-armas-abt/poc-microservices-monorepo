package com.demo.bbq.business.kitchenorder.infrastructure.repository.diningroomorder.dto;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DiningRoomOrderDtoRequest implements Serializable {

  private List<MenuOrderDtoRequest> menuOrderList;
  private Integer tableNumber;

}