package com.demo.bbq.business.payment.infrastructure.broker.consumer.dto;

import java.io.Serializable;
import java.util.List;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DiningRoomOrderDto implements Serializable {

  private Integer tableNumber;
  private List<MenuOrderDto> menuOrderList;

}
