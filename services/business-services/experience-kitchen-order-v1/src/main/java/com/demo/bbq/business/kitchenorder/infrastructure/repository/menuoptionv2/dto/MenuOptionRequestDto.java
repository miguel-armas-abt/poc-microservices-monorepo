package com.demo.bbq.business.kitchenorder.infrastructure.repository.menuoptionv2.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.*;

@Builder
@Setter
@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuOptionRequestDto implements Serializable {

  private String description;

  private String category;

  private BigDecimal price;

  private boolean isActive;
}
