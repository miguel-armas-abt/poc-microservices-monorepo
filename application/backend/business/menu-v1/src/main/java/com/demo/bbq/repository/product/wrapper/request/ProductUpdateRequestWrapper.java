package com.demo.bbq.repository.product.wrapper.request;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductUpdateRequestWrapper implements Serializable {

  private BigDecimal unitPrice;
  private String scope;
}
