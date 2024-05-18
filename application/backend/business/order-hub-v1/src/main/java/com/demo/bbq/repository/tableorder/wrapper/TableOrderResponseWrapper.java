package com.demo.bbq.repository.tableorder.wrapper;

import lombok.*;
import java.io.Serializable;
import java.util.List;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TableOrderResponseWrapper implements Serializable{

  private List<MenuOrderWrapper> menuOrderList;

  private Integer tableNumber;

  private String id;
}
