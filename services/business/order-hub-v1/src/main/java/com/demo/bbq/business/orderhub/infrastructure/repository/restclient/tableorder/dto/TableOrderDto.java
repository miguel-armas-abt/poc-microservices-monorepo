package com.demo.bbq.business.orderhub.infrastructure.repository.restclient.tableorder.dto;

import lombok.*;
import java.io.Serializable;
import java.util.List;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TableOrderDto implements Serializable{

  private List<MenuOrderDto> menuOrderList;

  private Integer tableNumber;

  private Long id;
}
