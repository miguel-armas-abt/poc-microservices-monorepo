package com.demo.bbq.business.orderhub.infrastructure.repository.restclient.tableorder.dto;

import java.io.Serializable;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MenuOrderRequestDto implements Serializable{

  private Long menuOptionId;

  private Integer quantity;
}
