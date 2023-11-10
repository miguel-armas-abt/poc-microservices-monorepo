package com.demo.bbq.business.tableplacement.domain.model.request;

import lombok.*;
import java.io.Serializable;
import java.util.List;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TableOrderRequest implements Serializable {

  private List<MenuOrderRequest> menuOrderList;
  private Integer tableNumber;

}
