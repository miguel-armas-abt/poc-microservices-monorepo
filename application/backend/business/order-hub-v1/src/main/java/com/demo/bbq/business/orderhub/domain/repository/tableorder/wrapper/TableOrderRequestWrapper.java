package com.demo.bbq.business.orderhub.domain.repository.tableorder.wrapper;

import lombok.*;
import java.io.Serializable;
import java.util.List;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TableOrderRequestWrapper implements Serializable{

  private List<MenuOrderWrapper> menuOrderList;

  private Integer tableNumber;

  private Long id;
}
