package com.demo.poc.entrypoint.rules.strategies.discount.dto;

import java.io.Serializable;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DiscountRuleRequestDto implements Serializable {

  private Integer quantity;
  private String productCode;
}
