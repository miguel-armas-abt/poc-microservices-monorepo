package com.demo.bbq.business.invoice.domain.model.response;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MenuOrder implements Serializable {

  private String description;
  private BigDecimal price;
  private Integer quantity;
  private BigDecimal total;
}
