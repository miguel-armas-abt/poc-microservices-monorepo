package com.demo.bbq.entrypoint.rules.strategies.discount.dto;

import java.io.Serializable;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DiscountRuleRequestDTO implements Serializable {

  private Integer quantity;
  private String productCode;
}
