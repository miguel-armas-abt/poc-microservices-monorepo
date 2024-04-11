package com.demo.bbq.business.invoice.domain.repository.product.wrapper;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseWrapper implements Serializable {

  private String code;
  private BigDecimal unitPrice;
}
