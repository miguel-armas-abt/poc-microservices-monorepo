package com.demo.bbq.business.diningroomorder.domain.model.response;

import lombok.*;
import java.io.Serializable;
import java.util.List;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DiningRoomOrder implements Serializable{

  private List<MenuOrder> menuOrderList;
  private Integer tableNumber;

}