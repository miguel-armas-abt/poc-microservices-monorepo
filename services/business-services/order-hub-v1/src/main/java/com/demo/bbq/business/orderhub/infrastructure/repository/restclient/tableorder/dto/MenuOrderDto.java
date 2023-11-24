package com.demo.bbq.business.orderhub.infrastructure.repository.restclient.tableorder.dto;

import lombok.*;
import java.io.Serializable;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MenuOrderDto implements Serializable {

  private String productCode;

  private Integer quantity;

}
