package com.demo.bbq.business.orderhub.infrastructure.repository.restclient.diningroomorder.dto;

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
