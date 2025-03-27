package com.demo.poc.entrypoint.rules.strategies.discount.dto;

import java.io.Serializable;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DiscountRuleResponseDTO implements Serializable {

  private Double discount;
}