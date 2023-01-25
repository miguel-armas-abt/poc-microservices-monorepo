package com.demo.bbq.business.diningroomorder.infrastructure.repository.restclient.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MenuOptionDto implements Serializable {

  private Long id;

  private String description;

  private String category;

  private BigDecimal price;

  private boolean active;

}
