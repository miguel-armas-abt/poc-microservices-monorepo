package com.demo.bbq.business.kitchenorder.infrastructure.repository.diningroomorder.dto;

import lombok.*;

import java.io.Serializable;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MenuOrderDtoRequest implements Serializable{

  private Long menuOptionId;
  private Integer quantity;

}
