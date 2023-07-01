package com.demo.bbq.business.diningroomorder.domain.model.dto;

import lombok.*;
import java.io.Serializable;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MenuOrderDto implements Serializable {

  private Long menuOptionId;
  private Integer quantity;

}
