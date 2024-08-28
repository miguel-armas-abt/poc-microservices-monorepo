package com.demo.bbq.entrypoint.calculator.repository.discount.wrapper;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DiscountResponseWrapper implements Serializable {

  private Double discount;
}
