package com.demo.bbq.business.waiterorder.infrastructure.repository.restclient.diningroomorder.dto;

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

  private Integer tableNumber;

  private Long id;
}
