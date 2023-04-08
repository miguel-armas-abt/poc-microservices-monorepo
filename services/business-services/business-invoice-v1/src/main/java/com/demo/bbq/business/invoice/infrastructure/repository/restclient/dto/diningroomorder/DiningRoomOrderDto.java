package com.demo.bbq.business.invoice.infrastructure.repository.restclient.dto.diningroomorder;

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
