package com.demo.bbq.business.diningroomorder.domain.model.request;

import java.io.Serializable;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MenuOrderRequest implements Serializable{

  private Long menuOptionId;
  private Integer quantity;

}
