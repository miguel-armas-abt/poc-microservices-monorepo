package com.demo.bbq.business.diningroomorder.domain.model.response;

import lombok.*;
import java.io.Serializable;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MenuOrder implements Serializable {

  private Long menuOptionId;
  private Integer quantity;

}
