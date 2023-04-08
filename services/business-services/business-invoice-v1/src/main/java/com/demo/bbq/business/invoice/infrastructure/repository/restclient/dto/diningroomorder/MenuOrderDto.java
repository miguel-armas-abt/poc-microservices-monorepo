package com.demo.bbq.business.invoice.infrastructure.repository.restclient.dto.diningroomorder;

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
