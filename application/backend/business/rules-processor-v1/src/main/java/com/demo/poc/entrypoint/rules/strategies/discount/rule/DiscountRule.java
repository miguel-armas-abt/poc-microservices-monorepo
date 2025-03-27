package com.demo.poc.entrypoint.rules.strategies.discount.rule;

import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;

import com.demo.poc.entrypoint.rules.base.rule.Rule;
import java.io.Serializable;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Setter
@Getter
@ToString
public class DiscountRule extends Rule implements Serializable {

  //input
  private Integer quantity;
  private String productCode;

  //output
  private boolean discountable;

  private double discount;

  public final boolean validateDiscountable(List<String> productCodeList) {
    return isNotEmpty(productCodeList) && productCodeList.contains(productCode);
  }
}