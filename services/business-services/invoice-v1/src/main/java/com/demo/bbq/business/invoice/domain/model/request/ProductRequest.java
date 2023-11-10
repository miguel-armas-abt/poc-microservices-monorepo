package com.demo.bbq.business.invoice.domain.model.request;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest implements Serializable {

  private String description;
  private Integer quantity;
  private BigDecimal unitPrice;

}
