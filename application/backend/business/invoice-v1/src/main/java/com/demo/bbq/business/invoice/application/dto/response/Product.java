package com.demo.bbq.business.invoice.application.dto.response;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Product implements Serializable {

  private String description;
  private Integer quantity;
  private BigDecimal unitPrice;
  private BigDecimal subtotal;

}
