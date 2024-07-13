package com.demo.bbq.entrypoint.calculator.dto.response;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO implements Serializable {

  private String description;
  private Integer quantity;
  private BigDecimal unitPrice;
  private BigDecimal subtotal;

}
