package com.demo.bbq.business.waiterorder.infrastructure.repository.restclient.diningroomorder.dto;

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
