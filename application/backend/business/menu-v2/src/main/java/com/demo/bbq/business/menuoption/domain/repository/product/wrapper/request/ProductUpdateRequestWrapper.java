package com.demo.bbq.business.menuoption.domain.repository.product.wrapper.request;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductUpdateRequestWrapper implements Serializable {

  private BigDecimal unitPrice;
  private String scope;
}
